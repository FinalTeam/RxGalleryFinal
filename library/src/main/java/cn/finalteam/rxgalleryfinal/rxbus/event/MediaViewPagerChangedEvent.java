package cn.finalteam.rxgalleryfinal.rxbus.event;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/25 下午3:45
 */
public class MediaViewPagerChangedEvent {

    private int curIndex;
    private int totalSize;
    private boolean isPreview;

    public MediaViewPagerChangedEvent(int curIndex, int totalSize, boolean isPreview) {
        this.curIndex = curIndex;
        this.totalSize = totalSize;
        this.isPreview = isPreview;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public boolean isPreview() {
        return isPreview;
    }
}
