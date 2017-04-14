package cn.finalteam.rxgalleryfinal.utils;

/**
 * Desction:文件工具类
 * Author:dujinyang
 */
public class FileUtils {

    /**
     * 验证是否是图片路径
     * @int
     */
    public static int existImageDir(String dir){
        String image = dir.trim().toString();
        int bk = image.lastIndexOf(".");
        return bk;
    }
}
