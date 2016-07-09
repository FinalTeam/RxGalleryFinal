package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.List;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/18 下午7:48
 */
public class MediaGridAdapter extends RecyclerView.Adapter<MediaGridAdapter.GridViewHolder> {

    private Context mContext;
    private List<MediaBean> mMediaBeanList;
    private LayoutInflater mInflater;
    private int mImageSize;
    private Configuration mConfiguration;
    private Drawable mDefaultImage;

    public MediaGridAdapter(Context context, List<MediaBean> list, int screenWidth, Configuration configuration) {
        this.mContext = context;
        this.mMediaBeanList = list;
        this.mInflater = LayoutInflater.from(context);
        this.mImageSize = screenWidth/3;
        this.mDefaultImage = context.getResources().getDrawable(R.drawable.ic_default_image);
        this.mConfiguration = configuration;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_media_grid_item, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        MediaBean mediaBean = mMediaBeanList.get(position);
        if(mediaBean.getId() == Integer.MIN_VALUE) {
            holder.mCbCheck.setVisibility(View.GONE);
            holder.mIvMediaImage.setVisibility(View.GONE);
            holder.mLlCamera.setVisibility(View.VISIBLE);
        } else {
            holder.mCbCheck.setVisibility(View.VISIBLE);
            holder.mIvMediaImage.setVisibility(View.VISIBLE);
            holder.mLlCamera.setVisibility(View.GONE);

            String path = mediaBean.getThumbnailSmallPath();
            if(TextUtils.isEmpty(path)) {
                path = mediaBean.getThumbnailBigPath();
            }
            if(TextUtils.isEmpty(path)) {
                path = mediaBean.getOriginalPath();
            }
            mConfiguration.getImageLoader()
                    .displayImage(mContext, path, holder.mIvMediaImage, mDefaultImage, mImageSize, mImageSize);
        }
    }

    @Override
    public int getItemCount() {
        return mMediaBeanList.size();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        RecyclerImageView mIvMediaImage;
        CheckBox mCbCheck;

        LinearLayout mLlCamera;

        public GridViewHolder(View itemView) {
            super(itemView);
            mIvMediaImage = (RecyclerImageView) itemView.findViewById(R.id.iv_media_image);
            mCbCheck = (CheckBox) itemView.findViewById(R.id.cb_check);
            mLlCamera = (LinearLayout) itemView.findViewById(R.id.ll_camera);
        }
    }

}
