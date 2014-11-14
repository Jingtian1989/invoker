package resolver.support;

import resolver.ResolverContext;
import resolver.Resolver;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class PrintWriterResolverSupport implements Resolver{
    @Override
    public Object resolve(ResolverContext context) {
        return context.getWriter();
    }
}
