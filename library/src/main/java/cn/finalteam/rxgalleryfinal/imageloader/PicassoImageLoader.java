package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Desction:
 * .centerCrop() 预览大图
 * Author:dujinyang
 * Date:16/6/17 下午1:23
 */
public class PicassoImageLoader implements AbsImageLoader {


    @Override
    public void displayImage(Context context, String path, FixImageView imageView, Drawable defaultDrawable, Bitmap.Config config, boolean resize, boolean isGif, int width, int height, int rotate) {
        RequestCreator creator = Picasso.with(context)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .rotate(rotate)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .config(config)
                .tag(context);
        if (resize) {
            creator = creator.resize(width, height)
                    .centerCrop();
        }
        creator.into(imageView);
    }
}
