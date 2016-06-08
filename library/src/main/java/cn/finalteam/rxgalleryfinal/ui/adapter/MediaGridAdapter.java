package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

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
    private int mRowHeight;
    private int mImageSize;
    public MediaGridAdapter(Context context, List<MediaBean> list, int screenWidth) {
        this.mContext = context;
        this.mMediaBeanList = list;
        this.mInflater = LayoutInflater.from(context);
        this.mRowHeight = screenWidth/3;
        this.mImageSize = screenWidth/4;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_media_grid_item, parent, false);
        return new GridViewHolder(view, mRowHeight);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        MediaBean mediaBean = mMediaBeanList.get(position);
        if(position == 0) {
            holder.mCbCheck.setVisibility(View.GONE);
            holder.mIvMediaImage.setVisibility(View.GONE);
            holder.mIvCamera.setVisibility(View.VISIBLE);
        } else {
            holder.mCbCheck.setVisibility(View.VISIBLE);
            holder.mIvMediaImage.setVisibility(View.VISIBLE);
            holder.mIvCamera.setVisibility(View.GONE);

            String path = mediaBean.getOriginalPath();
            Picasso.with(mContext)
                    .load(new File(path))
                    .placeholder(R.drawable.ic_gf_default_photo)
                    .error(R.drawable.ic_gf_default_photo)
                    .resize(mImageSize, mImageSize)
                    .tag(this)
                    .centerInside()
                    .into(holder.mIvMediaImage);
        }
    }

    @Override
    public int getItemCount() {
        return mMediaBeanList.size();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        RecyclerImageView mIvMediaImage;
        CheckBox mCbCheck;

        ImageView mIvCamera;

        public GridViewHolder(View itemView, int rowHeight) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rowHeight));
            mIvMediaImage = (RecyclerImageView) itemView.findViewById(R.id.iv_media_image);
            mCbCheck = (CheckBox) itemView.findViewById(R.id.cb_check);
            mIvCamera = (ImageView) itemView.findViewById(R.id.iv_camera);
        }
    }

}
