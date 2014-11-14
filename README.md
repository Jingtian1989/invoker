# invoker

invoker is a http service library. 


# how to use it

**step 1**
implement a invoker and annotate it with `HttpRequestHandler` which `requestUrl` stands for the 
invoker's serving url and `method` stands for the serving method.

**step 2**
annotate the serving method's parameter with `HttpRequestParameter` which `name` stands for the
http query parameter.

**step 3**
register the invokers with bootstrap and start it. 

the following code demonstrate a echo invoker. 

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

now, you can type **_http://localhost:8006/echo?string=helloworld_** in your brower and get the echo string.