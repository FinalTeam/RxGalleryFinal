package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Desction:媒体扫描工具类
 * Author:pengjianbo  Dujinyang
 * Date:16/6/8 上午11:36
 */
public class MediaScanner {
    private MediaScannerConnection mediaScanConn = null;
    private String fileType = null;
    private String filePath = null;
    private ScanCallback scanCallback;

    /**
     * 然后调用MediaScanner.scanFile("/sdcard/2.mp3");
     */
    public MediaScanner(Context context) {
        MusicScannerClient client;
        client = new MusicScannerClient();

        if (mediaScanConn == null) {
            mediaScanConn = new MediaScannerConnection(context, client);
        }
    }

    /**
     * 扫描文件标签信息
     *
     * @param filePath 文件路径
     * @param fileType 文件类型
     */

    public void scanFile(String filePath, String fileType, ScanCallback callback) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.scanCallback = callback;
        //连接之后调用MusicSannerClient的onMediaScannerConnected()方法
        mediaScanConn.connect();
    }

    public void unScanFile() {
        mediaScanConn.disconnect();
        scanCallback = null;
    }

    public interface ScanCallback {
        void onScanCompleted(String image, Uri uri);
    }

    private class MusicScannerClient implements MediaScannerConnection.MediaScannerConnectionClient {
        @Override
        public void onMediaScannerConnected() {
            Logger.i("onMediaScannerConnected");
            mediaScanConn.scanFile(filePath, fileType);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            Logger.i("onScanCompleted path=" + path + ", uri=" + uri);
            mediaScanConn.disconnect();
            if (scanCallback != null) {
                scanCallback.onScanCompleted(path, uri);
            }
            fileType = null;
            filePath = null;
        }
    }
}
