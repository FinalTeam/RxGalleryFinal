package cn.finalteam.rxgalleryfinal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.finalteam.rxgalleryfinal.R;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 下午5:56
 */
class DefaultLoadMoreView implements ILoadMoreView {

    private TextView mTvMessage;
    private ProgressBar mPbLoading;

    public DefaultLoadMoreView(Context context) {
//        super(context);
        init(context);
    }

    public DefaultLoadMoreView(Context context, AttributeSet attrs) {
//        super(context, attrs);
        init(context);
    }

    public DefaultLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
        init(context);
    }

    View view;
    private void init(Context context) {
        view = View.inflate(context, R.layout.loading_view_final_footer_default, null);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        mTvMessage = (TextView) view.findViewById(R.id.tv_loading_msg);
    }

    @Override
    public void showNormal() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.loading_view_click_loading_more);
    }

    @Override
    public void showNoMore() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.loading_view_no_more);
    }

    @Override
    public void showLoading() {
        mPbLoading.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.loading_view_loading);
    }

    @Override
    public View getFooterView() {
        return view;
    }

}
