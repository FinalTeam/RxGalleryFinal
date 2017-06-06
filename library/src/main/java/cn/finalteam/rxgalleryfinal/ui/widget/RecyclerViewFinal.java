package cn.finalteam.rxgalleryfinal.ui.widget;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.finalteam.rxgalleryfinal.R;


/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/3/7 下午6:40
 */
public class RecyclerViewFinal extends RecyclerView {

    /**
     * 是否可以加载跟多
     */
    private boolean mHasLoadMore;
    /**
     * 加载更多lock
     */
    private boolean mLoadMoreLock;
    /**
     * 加载更多事件回调
     */
    private OnLoadMoreListener mOnLoadMoreListener;
    /**
     * emptyview
     */
    private View mEmptyView;
    /**
     * 刷新数据时停止滑动,避免出现数组下标越界问题
     */
    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
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
    private FooterAdapter mFooterViewAdapter;
    private TextView mTvMessage;
    private ProgressBar mPbLoading;
    private View mFooterView;

    public RecyclerViewFinal(Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewFinal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerViewFinal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(Context context) {
        mFooterView = LayoutInflater.from(context).inflate(R.layout.gallery_loading_view_final_footer_default, null);
        mPbLoading = (ProgressBar) mFooterView.findViewById(R.id.pb_loading);
        mTvMessage = (TextView) mFooterView.findViewById(R.id.tv_loading_msg);

//        setHasLoadMore(false);
        addOnScrollListener(new RecyclerViewOnScrollListener());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        try {
            adapter.unregisterAdapterDataObserver(mDataObserver);
        } catch (Exception ignored) {
        }

        adapter.registerAdapterDataObserver(mDataObserver);
        mFooterViewAdapter = new FooterAdapter(adapter, mFooterView);

        if (getLayoutManager() != null) {
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
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 没有很多了
     */
    private void showNoMoreUI() {
        mLoadMoreLock = false;
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.gallery_loading_view_no_more);
    }

    /**
     * 显示默认UI
     */
    private void showNormalUI() {
        mLoadMoreLock = false;
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setText(R.string.gallery_loading_view_click_loading_more);
    }

    /**
     * 显示加载中UI
     */
    private void showLoadingUI() {
        mPbLoading.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.gallery_loading_view_loading);
    }

    /**
     * 是否有更多
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
    private void executeLoadMore() {
        if (!mLoadMoreLock && mHasLoadMore) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.loadMore();
            }
            mLoadMoreLock = true;//上锁
            showLoadingUI();
        }
    }

    public void setFooterViewHide(boolean footerViewHide) {
        if (footerViewHide) {
            mFooterView.setVisibility(View.GONE);
        } else {
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置OnItemClickListener
     */
    public void setOnItemClickListener(FooterAdapter.OnItemClickListener listener) {
        mFooterViewAdapter.setOnItemClickListener(listener);
    }

    public interface OnLoadMoreListener {
        void loadMore();
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

            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
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
                if (mHasLoadMore) {
                    executeLoadMore();
                }
            }
        }

        /**
         * 取数组中最大值
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
}
