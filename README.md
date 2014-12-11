# invoker

invoker is a tiny webservice library. 


# how to use it

**step 1**
implement a invoker and annotate it with **_HttpRequestHandler_** parameters **_requestUrl_** for the 
serving url and **_method_** for the serving method.

**step 2**
annotate the serving method's parameter with **_HttpRequestParameter_** parameters **_name_** for the
http query parameter.

**step 3**
register the invokers with bootstrap and start it. 

the following code demonstrate a simple echo invoker. 

```js
@HttpRequestHandler(requestUrl = "/echo", method = "service", description = 
			"echo what you type")
public class EchoInvoker {

    public void service(PrintWriter out, @HttpRequestParameter(name = "string")
    	 String in) {
        out.println(in);
    }

    public static void main(String args[]) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.addHttpInvoker(new EchoInvoker());
            bootstrap.start();
            bootstrap.await();
            bootstrap.stop();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

now, you can type **_http://localhost:8006/echo?string=helloworld_** in brower and get the echo string.