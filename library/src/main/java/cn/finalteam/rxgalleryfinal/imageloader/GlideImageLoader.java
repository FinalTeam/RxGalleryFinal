package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import cn.finalteam.rxgalleryfinal.imageloader.rotate.RotateTransformation;
import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by pengjianbo on 2016/8/13 0013.
 */
public class GlideImageLoader implements AbsImageLoader {

    @Override
    public void displayImage(Object context, String path, FixImageView imageView, Drawable defaultDrawable, boolean resize, int width, int height, int rotate) {
        Context ctx = (Context) context;
        Glide.with(ctx)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .crossFade()
                .transform(new RotateTransformation(ctx, rotate))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
}
