package cn.finalteam.rxgalleryfinal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/6/3 下午8:10
 */
public class RecyclerImageView extends ImageView {
    public RecyclerImageView(Context context) {
        super(context);
    }

    public RecyclerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }

}
