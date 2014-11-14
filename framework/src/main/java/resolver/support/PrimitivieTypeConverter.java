package resolver.support;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class PrimitivieTypeConverter {

    public static final Object convert(Class<?> clazz, String source) {
        if (clazz == String.class) {
            return source;
        }

        Object result = null;
        if (clazz == Boolean.class || clazz == boolean.class) {
            if (source == null) {
                if (clazz == Boolean.class) {
                    result = null;
                } else {
                    result = Boolean.FALSE;
                }
            } else {
                source = source.trim();
                result = Boolean.parseBoolean(source);
            }
        } else if (clazz == Character.class || clazz == char.class) {
            // char��Ĭ��ֵ��0, CharacterĬ��Ϊnull
            if (source == null) {
                if (clazz == Character.class) {
                    result = null;
                } else {
                    result = Character.valueOf((char) 0);
                }
            } else {
                source = source.trim();
                if (source.length() > 0) {
                    result = source.charAt(0);
                } else {
                    result = null;
                }
            }
        } else if (clazz == Short.class || clazz == short.class) {
            // short��Ĭ��ֵ��0��ShortĬ��Ϊnull
            if (source == null) {
                if (clazz == Short.class) {
                    result = null;
                } else {
                    result = Short.valueOf((short) 0);
                }
            } else {
                source = source.trim();
                if (source.length() > 0) {
                    result = Short.parseShort(source);
                } else {
                    result = null;
                }
            }
        } else if (clazz == Integer.class || clazz == int.class) {
            // int��Ĭ��ֵ��0��IntegerĬ��Ϊnull
            if (source == null) {
                if (clazz == Integer.class) {
                    result = null;
                } else {
                    result = Integer.valueOf(0);
                }
            } else {
                source = source.trim();
                if (source.length() > 0) {
                    result = Integer.parseInt(source);
                } else {
                    result = null;
                }
            }
        } else if (clazz == Long.class || clazz == long.class) {
            // long��Ĭ��ֵ��0L��LongĬ��Ϊnull
            if (source == null) {
                if (clazz == Long.class) {
                    result = null;
                } else {
                    result = Long.valueOf(0L);
                }
            } else {
                source = source.trim();
                if (source.length() > 0) {
                    result = Long.parseLong(source);
                } else {
                    result = null;
                }
            }
        } else if (clazz == Float.class || clazz == float.class) {
            // float��Ĭ��ֵ��0f, FloatĬ��ֵΪnull
            if (source == null) {
                if (clazz == Float.class) {
                    result = null;
                } else {
                    result = Float.valueOf(0F);
                }
            } else {
                source = source.trim();
                if (source.length() > 0) {
                    result = Float.valueOf(source);
                } else {
                    result = null;
                }
            }
        } else if (clazz == Double.class || clazz == double.class) {
            // double��Ĭ��ֵ��0f, DoubleĬ��ֵΪnull
            if (source == null) {
                if (clazz == Double.class) {
                    result = null;
                } else {
                    result = Double.valueOf(0D);
                }
            } else {
                source = source.trim();
                if (source.length() > 0) {
                    result = Double.valueOf(source);
                } else {
                    result = null;
                }
            }
        }
        return result;
    }
}
