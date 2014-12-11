package invoker;

import org.invoker.annotaion.HttpRequestHandler;
import org.invoker.annotaion.HttpRequestParameter;
import org.invoker.bootstrap.Bootstrap;

import java.io.PrintWriter;

/**
 * Created by jingtian.zjt on 2014/11/14.
 */

@HttpRequestHandler(requestUrl = "/echo", method = "service", description = "print out what you type")
public class InvokerTest {


    public void service(PrintWriter out, @HttpRequestParameter(name = "string") String in) {
        out.println(in);
    }

    public static void main(String args[]) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.addHttpInvoker(new InvokerTest());
            bootstrap.start();
            bootstrap.await();
            bootstrap.stop();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
