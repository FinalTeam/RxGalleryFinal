package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.BucketBean;
import cn.finalteam.rxgalleryfinal.ui.widget.SquareImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/4 下午5:40
 */
public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.BucketViewHolder>{

    private List<BucketBean> mBucketList;
    private Context mContext;
    private LayoutInflater mInflater;
    private Drawable mDefaultImage;
    private Configuration mConfiguration;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public BucketAdapter(Context context, List<BucketBean> bucketList, Configuration configuration) {
        this.mContext = context;
        this.mBucketList = bucketList;
        this.mConfiguration = configuration;

        this.mDefaultImage = new ColorDrawable(context.getResources().getColor(R.color.F2F2F2));
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public BucketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_bucket_item, parent, false);
        return new BucketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BucketViewHolder holder, int position) {
        BucketBean bucketBean = mBucketList.get(position);
        String bucketName = bucketBean.getBucketName();
        if(position != 0) {
            SpannableString nameSpannable = new SpannableString(bucketName + "\n" + bucketBean.getImageCount() + "张");
            nameSpannable.setSpan(new ForegroundColorSpan(Color.GRAY), bucketName.length(), nameSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            nameSpannable.setSpan(new RelativeSizeSpan(0.8f), bucketName.length(), nameSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mTvBucketName.setText(nameSpannable);
        } else {
            holder.mTvBucketName.setText(bucketName);
        }

        String path = bucketBean.getCover();
        mConfiguration.getImageLoader()
                .displayImage(mContext, path, holder.mIvBucketCover, mDefaultImage, 100, 100);
    }

    @Override
    public int getItemCount() {
        return mBucketList.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }

    class BucketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTvBucketName;
        SquareImageView mIvBucketCover;
        RadioButton mRbSelected;

        public BucketViewHolder(View itemView) {
            super(itemView);
            mTvBucketName = (TextView) itemView.findViewById(R.id.tv_bucket_name);
            mIvBucketCover = (SquareImageView) itemView.findViewById(R.id.iv_bucket_cover);
            mRbSelected = (RadioButton) itemView.findViewById(R.id.rb_selected);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnRecyclerViewItemClickListener != null) {
                mOnRecyclerViewItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

     public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view , int position);
    }
}
