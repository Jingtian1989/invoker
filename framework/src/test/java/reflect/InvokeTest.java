package reflect;

import java.lang.reflect.Method;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */
public class InvokeTest {

    public void testA(String a, int b) {
        System.out.println(a);
        System.out.println(b);
    }


    public static void main(String args[]) {
        Class<?>[] testAParaClass = new Class[2];
        testAParaClass[0] = String.class;
        testAParaClass[1] = int.class;

        Object[] testAPara = new Object[2];
        testAPara[0] = "abc";
        testAPara[1] = 1;

        try {
            Object invokeTest = InvokeTest.class.newInstance();
            Method testAMethod = InvokeTest.class.getMethod("testA", testAParaClass);
            testAMethod.invoke(invokeTest, testAPara);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
