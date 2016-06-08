package cn.finalteam.rxgalleryfinal;


import android.text.format.DateFormat;

import org.junit.Test;

import java.util.Date;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/6/3 上午10:30
 */
public class SimpleTest {

    @Test
    public void testDateFormat(){
        System.out.println("====" + DateFormat.format("yyyyMMddHHmmss", new Date()));
    }

}
