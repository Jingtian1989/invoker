package resolver;

import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class ResolverContext {

    private final Map<String, String> params;
    private final PrintWriter out;

    public ResolverContext(Map<String, String> params, PrintWriter out) {
        this.params = params;
        this.out = out;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public PrintWriter getWriter() {
        return out;
    }

}
