package org.invoker.resolver.support;

import org.invoker.resolver.Resolver;
import org.invoker.resolver.ResolverContext;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class PrimitiveResolverSupport implements Resolver {


    private String parameterName;

    private Class<?> targetType;

    @Override
    public Object resolve(ResolverContext context) {
        if (parameterName == null || targetType == null) {
            throw new IllegalArgumentException("must have a parameterName and targetType");
        }
        if (context.getParams() != null) {
            return PrimitivieTypeConverter.convert(targetType, context.getParams().get(parameterName));
        }
        return null;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }
}
