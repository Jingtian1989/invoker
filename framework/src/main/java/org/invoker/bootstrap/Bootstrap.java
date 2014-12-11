package org.invoker.bootstrap;

import com.sun.net.httpserver.HttpServer;
import org.invoker.invoker.HttpInvokerFacade;
import org.invoker.annotaion.HttpRequestHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class Bootstrap {

    private int port;

    private static final int DEFAULT_PORT = 8006;

    private HttpServer httpServer;

    private ConcurrentHashMap<String, HttpInvokerFacade> invokers;

    private ExecutorService executor;


    public Bootstrap(){
        port = DEFAULT_PORT;
        invokers = new ConcurrentHashMap<String, HttpInvokerFacade>();
        executor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors(), 5, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1000), TaskThreadFactory.createFactory("httpInvoker-worker",
                true, Thread.NORM_PRIORITY), new ThreadPoolExecutor.CallerRunsPolicy());

        addHttpInvoker(new IndexInvoker());
        addHttpInvoker(new UrlsInvoker());
        addHttpInvoker(new ShutdownInvoker(this));
    }

    public Bootstrap start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.setExecutor(executor);
        for (HttpInvokerFacade facade : invokers.values()) {
            httpServer.createContext(facade.getRequestUrl(), facade);
        }
        httpServer.start();
        return this;
    }

    public synchronized void await() {
        try {
            this.wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void wakeup() {
        this.notifyAll();
    }

    public synchronized Bootstrap stop() {
        if (executor != null) {
            executor.shutdown();
        }

        if (httpServer != null) {
            httpServer.stop(0);
        }
        return this;
    }


    public Bootstrap addHttpInvoker(Object httpInvoker) {
        if (httpInvoker != null) {
            HttpInvokerFacade facade = new HttpInvokerFacade(httpInvoker);
            invokers.put(facade.getRequestUrl(), facade);
        }
        return this;
    }

    public Collection<HttpInvokerFacade> listHttpInvokers() {
        return invokers.values();
    }


    private static class TaskThreadFactory implements ThreadFactory {

        private boolean daemon;

        private int threadPriority;

        private String threadName;

        private AtomicInteger number;

        private TaskThreadFactory(String threadName, boolean daemon, int threadPriority) {
            this.threadName = threadName;
            this.daemon = daemon;
            this.threadPriority = threadPriority;
            this.number = new AtomicInteger();
        }

        public static final ThreadFactory createFactory(String threadName, boolean daemon, int threadPriority) {

            if (threadName == null) {
                throw new IllegalArgumentException("[TaskThreadFactory] createFactory : must have threadName.");
            }
            return new TaskThreadFactory(threadName, daemon, threadPriority);
        }


        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(daemon);
            thread.setPriority(threadPriority);
            thread.setName(threadName + "-" + number.getAndDecrement());
            return thread;
        }
    }

    public static class StringUtils {
        public static String repeat(String str, int repeat) {
            StringBuffer buffer = new StringBuffer(repeat * str.length());
            for (int i = 0; i < repeat; i++) {
                buffer.append(str);
            }
            return buffer.toString();
        }

        public static String rightPad(String str, int size, String delim) {
            size = (size - str.length()) / delim.length();
            if (size > 0) {
                str += repeat(delim, size);
            }
            return str;
        }

        public static String leftPad(String str, int size, String delim) {
            size = (size - str.length()) / delim.length();
            if (size > 0) {
                str = repeat(delim, size) + str;
            }
            return str;
        }

        public static String center(String str, int size, String delim) {
            int sz = str.length();
            int p = size - sz;
            if (p < 1) {
                return str;
            }
            str = leftPad(str, sz + p / 2, delim);
            str = rightPad(str, size, delim);
            return str;
        }

        public static String rightPad(String str, int size) {
            return rightPad(str, size, " ");
        }

        public static String center(String str, int size) {
            return center(str, size, " ");
        }
    }


    @HttpRequestHandler(requestUrl = "/", method = "service", description = "root")
    public class IndexInvoker {
        public void service(PrintWriter out) {
            out.println(StringUtils.center("Hi, It's Http Invoker Proxy.", 70));
            out.println("You can type: ");
            out.println();
            out.println("http://localhost:" + port + "/urls for all urls.");
            out.println();
        }
    }

    @HttpRequestHandler(requestUrl = "/urls", method = "service", description = "list all registered invokers")
    public class UrlsInvoker {
        public void service(PrintWriter out) {
            for (HttpInvokerFacade httpInvokerFacade : listHttpInvokers()) {
                    out.println(httpInvokerFacade);
            }
        }
    }

    @HttpRequestHandler(requestUrl = "/shutdown", method = "service", description = "shutdown invoker server")
    public class ShutdownInvoker {

        private Bootstrap bootstrap;
        public ShutdownInvoker (Bootstrap bootstrap) {
            this.bootstrap = bootstrap;
        }

        public void service(PrintWriter out) {
            out.println("Goodbye, Http Invoker is shuting down!");
            bootstrap.wakeup();
        }
    }
}
