package org.invoker.resolver.support;

import org.invoker.resolver.Resolver;
import org.invoker.resolver.ResolverContext;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class PrintWriterResolverSupport implements Resolver {
    @Override
    public Object resolve(ResolverContext context) {
        return context.getWriter();
    }
}
