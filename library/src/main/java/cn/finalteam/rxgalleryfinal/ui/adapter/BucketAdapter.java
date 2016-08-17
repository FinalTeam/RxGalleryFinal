package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
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
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;

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
    private BucketBean mSelectedBucket;

    public BucketAdapter(Context context, List<BucketBean> bucketList, Configuration configuration) {
        this.mContext = context;
        this.mBucketList = bucketList;
        this.mConfiguration = configuration;
        this.mDefaultImage = new ColorDrawable(context.getResources().getColor(R.color.gallery_bucket_list_item_normal_color));
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public BucketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gallery_adapter_bucket_item, parent, false);
        return new BucketViewHolder(mContext, parent, view);
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
        if(mSelectedBucket != null && TextUtils.equals(mSelectedBucket.getBucketId(), bucketBean.getBucketId())) {
            holder.mRbSelected.setVisibility(View.VISIBLE);
            holder.mRbSelected.setChecked(true);
        } else {
            holder.mRbSelected.setVisibility(View.GONE);
        }

        String path = bucketBean.getCover();
        mConfiguration.getImageLoader()
                .displayImage(mContext, path, holder.mIvBucketCover, mDefaultImage, mConfiguration.getImageConfig(),
                        true, 100, 100, bucketBean.getOrientation());
    }

    public void setSelectedBucket(BucketBean bucketBean) {
        this.mSelectedBucket = bucketBean;
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
        AppCompatRadioButton mRbSelected;

        private ViewGroup mParentView;

        public BucketViewHolder(Context context, ViewGroup parent, View itemView) {
            super(itemView);
            this.mParentView = parent;
            mTvBucketName = (TextView) itemView.findViewById(R.id.tv_bucket_name);
            mIvBucketCover = (SquareImageView) itemView.findViewById(R.id.iv_bucket_cover);
            mRbSelected = (AppCompatRadioButton) itemView.findViewById(R.id.rb_selected);

            itemView.setOnClickListener(this);

            int checkTint = ThemeUtils.resolveColor(context, R.attr.gallery_checkbox_button_tint_color, R.color.gallery_default_checkbox_button_tint_color);
            CompoundButtonCompat.setButtonTintList(mRbSelected, ColorStateList.valueOf(checkTint));
        }

        @Override
        public void onClick(View v) {
            if(mOnRecyclerViewItemClickListener != null) {
                mOnRecyclerViewItemClickListener.onItemClick(v, getLayoutPosition());
            }

            setRadioDisChecked(mParentView);
            mRbSelected.setVisibility(View.VISIBLE);
            mRbSelected.setChecked(true);
        }

        /**
         * 设置未所有Item为未选中
         * @param parentView
         */
        private void setRadioDisChecked(ViewGroup parentView) {
            if (parentView == null || parentView.getChildCount() < 1) {
                return;
            }

            for (int i = 0; i < parentView.getChildCount(); i++) {
                View itemView = parentView.getChildAt(i);
                RadioButton rbSelect = (RadioButton) itemView.findViewById(R.id.rb_selected);
                if(rbSelect!=null){
                    rbSelect.setVisibility(View.GONE);
                    rbSelect.setChecked(false);
                }
            }
        }
    }

     public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view , int position);
    }
}
