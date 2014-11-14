package bootstrap;

import org.junit.Test;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */

public class BootstrapTest {


    @Test
    public void BootstrapTest() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.start();
            bootstrap.await();
            bootstrap.stop();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
