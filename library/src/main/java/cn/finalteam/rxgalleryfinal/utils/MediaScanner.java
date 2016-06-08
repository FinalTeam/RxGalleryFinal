package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Desction:媒体扫描工具类
 * Author:pengjianbo
 * Date:16/6/8 上午11:36
 */
public class MediaScanner {
    private MediaScannerConnection mediaScanConn = null;
    private MusicSannerClient client = null;
    private String fileType = null;
    private String[] filePaths = null;

    /**
     * 然后调用MediaScanner.scanFile("/sdcard/2.mp3");
     */
    public MediaScanner(Context context) {
        if (client == null) {
            client = new MusicSannerClient();
        }

        if (mediaScanConn == null) {
            mediaScanConn = new MediaScannerConnection(context, client);
        }
    }

    class MusicSannerClient implements MediaScannerConnection.MediaScannerConnectionClient {
        @Override
        public void onMediaScannerConnected() {

            if (filePaths != null) {
                for (String file : filePaths) {
                    mediaScanConn.scanFile(file, fileType);
                }
            }

            fileType = null;
            filePaths = null;
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mediaScanConn.disconnect();
        }
    }

    /**
     * 扫描文件标签信息
     * @param filePath 文件路径
     * @param fileType 文件类型
     */

    public void scanFile(String filePath, String fileType) {
        this.filePaths = new String[]{filePath};
        this.fileType = fileType;
        //连接之后调用MusicSannerClient的onMediaScannerConnected()方法
        mediaScanConn.connect();
    }

    /**
     * @param filePaths 文件路径
     * @param fileType 文件类型
     */
    public void scanFile(String[] filePaths, String fileType) {
        this.filePaths = filePaths;
        this.fileType = fileType;
        mediaScanConn.connect();
    }

    public void unScanFile(){
        mediaScanConn.disconnect();
    }
}
