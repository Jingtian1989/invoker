package bootstrap;

/**
 * Created by jingtian.zjt on 2014/11/13.
 */

import org.junit.Test;

import java.util.Scanner;

public class BootstrapTest {


    @Test
    public void BootstrapTest() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.start();
            try {
                Scanner scanner = new Scanner(System.in);
                String command = null;
                while (scanner.hasNextLine()) {
                    command = scanner.nextLine();
                    if (command.equals("shutdown")) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            bootstrap.stop();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
