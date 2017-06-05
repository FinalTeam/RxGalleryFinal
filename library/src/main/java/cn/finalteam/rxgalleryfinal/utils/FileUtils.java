package cn.finalteam.rxgalleryfinal.utils;

/**
 * Desction:文件工具类
 * Author:dujinyang
 */
public class FileUtils {

    /**
     * 验证是否是图片路径
     */
    public static int existImageDir(String dir) {
        return dir.trim().lastIndexOf(".");
    }
}
