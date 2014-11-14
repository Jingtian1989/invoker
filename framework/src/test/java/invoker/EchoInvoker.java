package invoker;

import annotation.HttpRequestHandler;
import annotation.HttpRequestParameter;
import bootstrap.Bootstrap;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by jingtian.zjt on 2014/11/14.
 */

@HttpRequestHandler(requestUrl = "/echo", method = "service", description = "print out what you type")
public class EchoInvoker {


    public void service(PrintWriter out, @HttpRequestParameter(name = "string") String in) {
        out.println(in);
    }

    public static void main(String args[]) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.addHttpInvoker(new EchoInvoker());
            bootstrap.start();
            bootstrap.await();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
