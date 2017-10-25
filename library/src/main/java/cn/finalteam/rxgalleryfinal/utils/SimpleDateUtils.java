package cn.finalteam.rxgalleryfinal.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * Created by KARL-dujinyang on 2017-04-13.
 */
public class SimpleDateUtils {

    public static String getNowTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return dateFormat.format(new Date());
    }
}
