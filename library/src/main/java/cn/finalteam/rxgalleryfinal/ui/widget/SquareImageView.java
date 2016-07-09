package cn.finalteam.rxgalleryfinal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/9 上午12:53
 */
public class SquareImageView extends FixImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec) {
        super .onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}
