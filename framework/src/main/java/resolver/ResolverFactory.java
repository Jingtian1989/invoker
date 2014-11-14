package resolver;

import annotation.HttpRequestParameter;
import annotation.HttpRequestParameter;
import resolver.support.PrimitiveResolverSupport;
import resolver.support.PrintWriterResolverSupport;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class ResolverFactory {

    private static final Map<Class<?>, Resolver> TYPE_MAPPING = new HashMap<Class<?>, Resolver>();

    static {
        TYPE_MAPPING.put(PrintWriter.class, new PrintWriterResolverSupport());
    }

    private static final Resolver DUMMY_RESOLVER = new DummyResolver();


    public static final Resolver[] createResolvers(Method method) {
        Resolver[] resolvers = new Resolver[0];
        if (method != null) {
            Class<?>[] paramClazzArray = method.getParameterTypes();
            Annotation[][] paramAnnocations = method.getParameterAnnotations();
            if (paramClazzArray != null && paramClazzArray.length > 0) {
                resolvers = new Resolver[paramClazzArray.length];
                for (int i = 0; i < paramClazzArray.length; i++) {
                    Class<?> paramClazz = paramClazzArray[i];
                    Resolver resolver = TYPE_MAPPING.get(paramClazz);
                    if (resolver != null) {
                        resolvers[i] = resolver;
                    } else {
                        Annotation parameterAnnocation = null;
                        if (paramAnnocations != null) {
                            Annotation[] column = paramAnnocations[i];
                            if (column != null) {
                                for (Annotation annotation : column) {
                                    if (annotation.annotationType() == HttpRequestParameter.class) {
                                        parameterAnnocation = annotation;
                                        break;
                                    }
                                }
                            }
                        }

                        if (parameterAnnocation == null) {
                            resolvers[i] = DUMMY_RESOLVER;
                        } else {
                            HttpRequestParameter parameter = (HttpRequestParameter) parameterAnnocation;
                            PrimitiveResolverSupport parameterResolver = new PrimitiveResolverSupport();
                            parameterResolver.setParameterName(parameter.name());
                            parameterResolver.setTargetType(paramClazz);
                            resolvers[i] = parameterResolver;
                        }
                    }
                }
            }
        }
        return resolvers;
    }

    public static class DummyResolver implements Resolver {
        @Override
        public Object resolve(ResolverContext context) {
            return null;
        }
    }


}
