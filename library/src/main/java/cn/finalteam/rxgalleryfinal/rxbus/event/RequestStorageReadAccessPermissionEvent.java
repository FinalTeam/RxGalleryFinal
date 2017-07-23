package cn.finalteam.rxgalleryfinal.rxbus.event;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/30 下午11:23
 */
public class RequestStorageReadAccessPermissionEvent {

    public static final int TYPE_CAMERA = 0;
    public static final int TYPE_WRITE = 1;

    private final boolean success;
    private final int type;

    public RequestStorageReadAccessPermissionEvent(boolean success, int type) {
        this.success = success;
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getType() {
        return type;
    }

}
