package cn.finalteam.rxgalleryfinal.rxbus.event;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/25 下午3:45
 */
public class MediaViewPagerChangedEvent {

    private int curIndex;
    private int totalSize;

    public MediaViewPagerChangedEvent(int curIndex, int totalSize) {
        this.curIndex = curIndex;
        this.totalSize = totalSize;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public int getTotalSize() {
        return totalSize;
    }
}
