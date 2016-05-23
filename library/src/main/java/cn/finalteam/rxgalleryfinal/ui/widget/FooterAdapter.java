package cn.finalteam.rxgalleryfinal.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Desction:footer适配器
 * Author:pengjianbo
 * Date:16/5/21 下午3:59
 */
public class FooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_FOOTER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    private RecyclerView.Adapter mAdapter;
    private View mFooterView;
    private OnItemClickListener mOnItemClickListener;

    public FooterAdapter(RecyclerView.Adapter adapter, View footerView) {
        this.mAdapter = adapter;
        this.mAdapter.registerAdapterDataObserver(mDataObserver);
        this.mFooterView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_FOOTER) {
            return new FooterViewHolder(mFooterView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isFooter(position)) {
        } else {
            if(mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder, position));
            }
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    /**
     * 设置Item点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isFooter(position)?ITEM_VIEW_TYPE_FOOTER:ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isFooter(int position) {
        return position == getItemCount() - 1;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }
    };

}
