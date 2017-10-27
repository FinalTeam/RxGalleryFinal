package cn.finalteam.rxgalleryfinal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/7 上午9:45
 */
public class IOUtils {

    public static void close(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void flush(OutputStream stream) {
        if (stream != null) {
            try {
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
