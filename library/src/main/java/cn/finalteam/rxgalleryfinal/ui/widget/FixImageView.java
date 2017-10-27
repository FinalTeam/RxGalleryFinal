package cn.finalteam.rxgalleryfinal.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Desction:为了兼容fresco框架而自定义的ImageView
 * Author:pengjianbo  Dujinyang
 * Date:2015/12/24 0024 20:14
 */
public class FixImageView extends AppCompatImageView {

    private OnImageViewListener mOnImageViewListener;

    public FixImageView(Context context) {
        super(context);
    }

    public FixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnImageViewListener(OnImageViewListener listener) {
        mOnImageViewListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOnImageViewListener != null) {
            mOnImageViewListener.onDetach();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mOnImageViewListener != null) {
            mOnImageViewListener.onAttach();
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable dr) {
        if (mOnImageViewListener != null) {
            if (mOnImageViewListener.verifyDrawable(dr)) {
                return true;
            }
        }
        return super.verifyDrawable(dr);
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        if (mOnImageViewListener != null) {
            mOnImageViewListener.onDetach();
        }
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        if (mOnImageViewListener != null) {
            mOnImageViewListener.onAttach();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnImageViewListener != null) {

            mOnImageViewListener.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOnImageViewListener == null) {
            return super.onTouchEvent(event);
        }
        return mOnImageViewListener.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public interface OnImageViewListener {
        void onDetach();

        void onAttach();

        boolean verifyDrawable(Drawable dr);

        void onDraw(Canvas canvas);

        boolean onTouchEvent(MotionEvent event);
    }

}
