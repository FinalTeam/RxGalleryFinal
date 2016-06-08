package cn.finalteam.rxgalleryfinal;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println("====" + format.format(new Date()));
    }
}