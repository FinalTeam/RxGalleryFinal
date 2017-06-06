package cn.finalteam.rxgalleryfinal.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Desction:footer适配器
 * Author:pengjianbo  Dujinyang
 * Date:16/5/21 下午3:59
 */
public class FooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_FOOTER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    private final RecyclerView.Adapter mAdapter;
    private final View mFooterView;
    private OnItemClickListener mOnItemClickListener;

    public FooterAdapter(RecyclerView.Adapter adapter, View footerView) {
        this.mAdapter = adapter;
        this.mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }
        });
        this.mFooterView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return new FooterViewHolder(mFooterView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isFooter(position)) {
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder, position));
            }
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    /**
     * 设置Item点击事件
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isFooter(position) ? ITEM_VIEW_TYPE_FOOTER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isFooter(int position) {
        return position == getItemCount() - 1;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}
