package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaCheckChangeEvent;
import cn.finalteam.rxgalleryfinal.rxjob.Job;
import cn.finalteam.rxgalleryfinal.rxjob.RxJob;
import cn.finalteam.rxgalleryfinal.rxjob.job.ImageThmbnailJobCreate;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerImageView;
import cn.finalteam.rxgalleryfinal.utils.OsCompat;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/18 下午7:48
 */
public class MediaGridAdapter extends RecyclerView.Adapter<MediaGridAdapter.GridViewHolder> {

    private MediaActivity mMediaActivity;
    private List<MediaBean> mMediaBeanList;
    private LayoutInflater mInflater;
    private int mImageSize;
    private Configuration mConfiguration;
    private Drawable mDefaultImage;
    private Drawable mImageViewBg;
    private Drawable mCameraImage;
    private int mCameraTextColor;
    public MediaGridAdapter(MediaActivity mediaActivity, List<MediaBean> list, int screenWidth, Configuration configuration) {
        this.mMediaActivity = mediaActivity;
        this.mMediaBeanList = list;
        this.mInflater = LayoutInflater.from(mediaActivity);
        this.mImageSize = screenWidth/3;
        int defaultResId = ThemeUtils.resolveDrawableRes(mediaActivity, R.attr.gallery_default_image, R.drawable.gallery_default_image);
        this.mDefaultImage = mediaActivity.getResources().getDrawable(defaultResId);
        this.mConfiguration = configuration;

        this.mImageViewBg = ThemeUtils.resolveDrawable(mMediaActivity,
                R.attr.gallery_imageview_bg, R.drawable.gallery_default_image);
        this.mCameraImage = ThemeUtils.resolveDrawable(mMediaActivity, R.attr.gallery_camera_bg,
                R.drawable.gallery_ic_camera);
        this.mCameraTextColor = ThemeUtils.resolveColor(mMediaActivity, R.attr.gallery_take_image_text_color,
                R.color.gallery_default_take_image_text_color);
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gallery_adapter_media_grid_item, parent, false);
        return new GridViewHolder(mMediaActivity, view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        MediaBean mediaBean = mMediaBeanList.get(position);
        if(mediaBean.getId() == Integer.MIN_VALUE) {
            holder.mCbCheck.setVisibility(View.GONE);
            holder.mIvMediaImage.setVisibility(View.GONE);
            holder.mLlCamera.setVisibility(View.VISIBLE);
            holder.mIvCameraImage.setImageDrawable(mCameraImage);
            holder.mTvCameraTxt.setTextColor(mCameraTextColor);
        } else {
            if(mConfiguration.isRadio()) {
                holder.mCbCheck.setVisibility(View.GONE);
            } else{
                holder.mCbCheck.setVisibility(View.VISIBLE);
                holder.mCbCheck.setOnClickListener(new OnCheckBoxClickListener(mediaBean));
            }
            holder.mIvMediaImage.setVisibility(View.VISIBLE);
            holder.mLlCamera.setVisibility(View.GONE);
            if(mMediaActivity.getCheckedList() != null && mMediaActivity.getCheckedList().contains(mediaBean)){
                holder.mCbCheck.setChecked(true);
            } else {
                holder.mCbCheck.setChecked(false);
            }
            String bitPath = mediaBean.getThumbnailSmallPath();
            String smallPath = mediaBean.getThumbnailSmallPath();

            if(!new File(bitPath).exists() || !new File(smallPath).exists()) {
                Job job = new ImageThmbnailJobCreate(mMediaActivity, mediaBean).create();
                RxJob.getDefault().addJob(job);
            }
            String path = mediaBean.getThumbnailSmallPath();
            if(TextUtils.isEmpty(path)) {
                path = mediaBean.getThumbnailBigPath();
            }
            if(TextUtils.isEmpty(path)) {
                path = mediaBean.getOriginalPath();
            }

            OsCompat.setBackgroundDrawableCompat(holder.mIvMediaImage, mImageViewBg);

            mConfiguration.getImageLoader()
                    .displayImage(mMediaActivity, path, holder.mIvMediaImage, mDefaultImage, mConfiguration.getImageConfig(),
                            true, mImageSize, mImageSize, mediaBean.getOrientation());
        }
    }

    @Override
    public int getItemCount() {
        return mMediaBeanList.size();
    }

    class OnCheckBoxClickListener implements View.OnClickListener {

        private MediaBean mediaBean;

        public OnCheckBoxClickListener(MediaBean bean) {
            this.mediaBean = bean;
        }

        @Override
        public void onClick(View view) {
            if(mConfiguration.getMaxSize() == mMediaActivity.getCheckedList().size() &&
                    !mMediaActivity.getCheckedList().contains(mediaBean)) {
                AppCompatCheckBox checkBox = (AppCompatCheckBox) view;
                checkBox.setChecked(false);
                Toast.makeText(mMediaActivity, mMediaActivity.getResources()
                        .getString(R.string.gallery_image_max_size_tip, mConfiguration.getMaxSize()), Toast.LENGTH_SHORT).show();
            } else {
                RxBus.getDefault().post(new MediaCheckChangeEvent(mediaBean));
            }
        }
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        RecyclerImageView mIvMediaImage;
        AppCompatCheckBox mCbCheck;

        LinearLayout mLlCamera;
        TextView mTvCameraTxt;
        ImageView mIvCameraImage;

        public GridViewHolder(Context context, View itemView) {
            super(itemView);
            mIvMediaImage = (RecyclerImageView) itemView.findViewById(R.id.iv_media_image);
            mCbCheck = (AppCompatCheckBox) itemView.findViewById(R.id.cb_check);

            mLlCamera = (LinearLayout) itemView.findViewById(R.id.ll_camera);
            mTvCameraTxt = (TextView) itemView.findViewById(R.id.tv_camera_txt);
            mIvCameraImage = (ImageView) itemView.findViewById(R.id.iv_camera_image);

            int checkTint = ThemeUtils.resolveColor(context, R.attr.gallery_checkbox_button_tint_color, R.color.gallery_default_checkbox_button_tint_color);
            CompoundButtonCompat.setButtonTintList(mCbCheck, ColorStateList.valueOf(checkTint));
        }
    }

}
