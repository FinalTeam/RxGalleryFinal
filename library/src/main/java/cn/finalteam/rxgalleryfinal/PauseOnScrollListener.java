package cn.finalteam.rxgalleryfinal;

import android.support.v7.widget.RecyclerView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/9 0009 18:23
 */
public abstract class PauseOnScrollListener extends RecyclerView.OnScrollListener {


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            resume();
        } else {
            pause();
        }
    }

    public abstract void resume();
    public abstract void pause();
}
