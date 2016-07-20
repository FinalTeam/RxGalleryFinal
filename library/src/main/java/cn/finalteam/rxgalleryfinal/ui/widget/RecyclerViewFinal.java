package cn.finalteam.rxgalleryfinal.ui.widget;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.finalteam.rxgalleryfinal.R;


/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/7 下午6:40
 */
public class RecyclerViewFinal extends RecyclerView {

    /**
     * 加载更多lock
     */
    private boolean mLoadMoreLock;
    /**
     * 是否可以加载跟多
     */
    boolean mHasLoadMore;

    /**
     * 加载更多事件回调
     */
    private OnLoadMoreListener mOnLoadMoreListener;

    /**
     * emptyview
     */
    private View mEmptyView;

    private FooterAdapter mFooterViewAdapter;

    private TextView mTvMessage;
    private ProgressBar mPbLoading;
    private View mFooterView;

    public RecyclerViewFinal(Context context) {
        super(context);
        init(context, null);
    }

    public RecyclerViewFinal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecyclerViewFinal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mFooterView = LayoutInflater.from(context).inflate(R.layout.gallery_loading_view_final_footer_default, null);
        mPbLoading = (ProgressBar) mFooterView.findViewById(R.id.pb_loading);
        mTvMessage = (TextView) mFooterView.findViewById(R.id.tv_loading_msg);

//        setHasLoadMore(false);
        addOnScrollListener(new RecyclerViewOnScrollListener());
    }

    Adapter adapter;
    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        try {
            adapter.unregisterAdapterDataObserver(mDataObserver);
        } catch (Exception e){}

        adapter.registerAdapterDataObserver(mDataObserver);
        mFooterViewAdapter = new FooterAdapter(adapter, mFooterView);

        if(getLayoutManager() != null) {
            GridLayoutManager manager = (GridLayoutManager) getLayoutManager();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mFooterViewAdapter.isFooter(position) ? manager.getSpanCount() : 1;
                }
            });
        }

        super.setAdapter(mFooterViewAdapter);
    }

    /**
     * 设置recyclerview emptyview
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 没有很多了
     */
    void showNoMoreUI() {
        mLoadMoreLock = false;
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.gallery_loading_view_no_more);
    }

    /**
     * 显示默认UI
     */
    void showNormalUI() {
        mLoadMoreLock = false;
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.gallery_loading_view_click_loading_more);
    }

    /**
     * 显示加载中UI
     */
    void showLoadingUI(){
        mPbLoading.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.gallery_loading_view_loading);
    }

    /**
     * 是否有更多
     * @param hasLoadMore
     */
    public void setHasLoadMore(boolean hasLoadMore) {
        mHasLoadMore = hasLoadMore;
        if (!mHasLoadMore) {
            showNoMoreUI();
        } else {
            showNormalUI();
        }
    }

    /**
     * 设置加载更多事件回调
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mOnLoadMoreListener = loadMoreListener;
    }

    /**
     * 完成加载更多
     */
    public void onLoadMoreComplete() {
        if (mHasLoadMore) {
            showNormalUI();
        }
    }

    /**
     * 加载更多
     */
    void executeLoadMore() {
        if(!mLoadMoreLock && mHasLoadMore) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.loadMore();
            }
            mLoadMoreLock = true;//上锁
            showLoadingUI();
        }
    }

    public void setFooterViewHide(boolean footerViewHide) {
        if(footerViewHide) {
            mFooterView.setVisibility(View.GONE);
        } else {
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置OnItemClickListener
     * @param listener
     */
    public void setOnItemClickListener(FooterAdapter.OnItemClickListener listener) {
        mFooterViewAdapter.setOnItemClickListener(listener);
    }

    /**
     * 滚动到底部自动加载更多数据
     */
    private class RecyclerViewOnScrollListener extends OnScrollListener {

        /**
         * 最后一个的位置
         */
        private int[] lastPositions;

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;

        /**
         * 当前滑动的状态
         */
        private int currentScrollState = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentScrollState = newState;
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                if(mHasLoadMore) {
                    executeLoadMore();
                }
            }
        }

        /**
         * 取数组中最大值
         *
         * @param lastPositions
         * @return
         */
        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }

            return max;
        }
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }


    /**
     * 刷新数据时停止滑动,避免出现数组下标越界问题
     */
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && mEmptyView != null) {
                if(adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }

            dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }
    };
}
