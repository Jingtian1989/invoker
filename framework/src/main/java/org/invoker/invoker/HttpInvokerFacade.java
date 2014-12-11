package org.invoker.invoker;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.invoker.annotaion.HttpRequestHandler;
import org.invoker.annotaion.HttpRequestParameter;
import org.invoker.resolver.Resolver;
import org.invoker.resolver.ResolverContext;
import org.invoker.resolver.ResolverFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class HttpInvokerFacade implements HttpHandler {

    private Object target;

    private String requestUrl;

    private Method service;

    private Resolver[] resolvers;

    private List<HttpRequestParameter> parameterLists;

    public HttpInvokerFacade(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("target can't be null");
        }

        this.target = target;
        Class<?> clazz = target.getClass();
        analyzeInvoker(clazz);
        resolvers = ResolverFactory.createResolvers(service);
    }

    private void analyzeInvoker(Class<?> clazz) {
        HttpRequestHandler invoker = clazz.getAnnotation(HttpRequestHandler.class);
        if (invoker == null) {
            throw new IllegalArgumentException("target must annotate #RequestUrl");
        }
        requestUrl = invoker.requestUrl();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(invoker.method())) {
                service = method;
                break;
            }
        }
        parameterLists = new LinkedList<HttpRequestParameter>();
        if (service != null) {
            Annotation[][] parameterAnnotations = service.getParameterAnnotations();
            for (Annotation[] annotations : parameterAnnotations) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == HttpRequestParameter.class) {
                        HttpRequestParameter parameter = (HttpRequestParameter) annotation;
                        parameterLists.add(parameter);
                    }
                }
            }
        }
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(requestUrl);
        if (parameterLists.size() > 0) {
            sb.append("?");
            boolean first = true;
            for (HttpRequestParameter parameter: parameterLists) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(parameter.name()).append("=").append("#");
            }
        }
        return sb.toString();
    }

    private Map<String, String> query2Map(String query) {
        Map<String, String> result = new HashMap<String, String>();
        if (query != null && query.trim().length() > 0) {
            for (String parameter : query.split("&")) {
                String pair[] = parameter.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                } else {
                    result.put(pair[0], null);
                }
            }
        }
        return result;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> parameters = query2Map(httpExchange.getRequestURI().getQuery());
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        ResolverContext context = new ResolverContext(parameters, out);
        OutputStream os = httpExchange.getResponseBody();
        try {
            Object[] args = new Object[] {};
            if (resolvers.length > 0) {
                args = new Object[resolvers.length];
                for (int i = 0; i < resolvers.length; i++) {
                    args[i] = resolvers[i].resolve(context);
                }
            }
            try {
                service.invoke(target, args);
            } catch (IllegalArgumentException ex) {
                out.println("IllegalArgument:");
                out.println(this);
            } catch (Exception ex) {
                out.println("UnkownError:");
                out.println("Exception: " + ex);
                out.println(this);
            }
            out.flush();
            StringBuffer sb = writer.getBuffer();
            httpExchange.sendResponseHeaders(200, sb.length());
            os.write(sb.toString().getBytes());
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (out != null) {
                out.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
}
