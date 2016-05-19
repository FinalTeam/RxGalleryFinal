package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/18 下午7:48
 */
public class MediaGridAdapter extends RecyclerView.Adapter<MediaGridAdapter.GridViewHolder> {

    private Context mContext;
    private List<MediaBean> mMediaBeanList;
    private LayoutInflater mInflater;
    private int mScreenWidth;
    private int mImageSize;

    public MediaGridAdapter(Context context, List<MediaBean> list, int screenWidth) {
        this.mContext = context;
        this.mMediaBeanList = list;
        this.mInflater = LayoutInflater.from(context);
        this.mScreenWidth = screenWidth;
        this.mImageSize = screenWidth/4;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_media_grid_item, parent, false);
        return new GridViewHolder(view, mScreenWidth);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        MediaBean mediaBean = mMediaBeanList.get(position);
        String path = mediaBean.getOriginalPath();
        Picasso.with(mContext)
                .load(new File(path))
                .resize(mImageSize, mImageSize)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.mIvMediaImage);
    }

    @Override
    public int getItemCount() {
        return mMediaBeanList.size();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvMediaImage;
        CheckBox mCbCheck;

        public GridViewHolder(View itemView, int screenWidth) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenWidth/2));
            mIvMediaImage = (ImageView) itemView.findViewById(R.id.iv_media_image);
            mCbCheck = (CheckBox) itemView.findViewById(R.id.cb_check);
        }
    }

}
