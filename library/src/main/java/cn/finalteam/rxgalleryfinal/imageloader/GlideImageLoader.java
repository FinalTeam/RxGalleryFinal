package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import cn.finalteam.rxgalleryfinal.imageloader.rotate.RotateTransformation;
import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by pengjianbo  Dujinyang on 2016/8/13 0013.
 */
public class GlideImageLoader implements AbsImageLoader {


    @Override
    public void displayImage(Context context, String path, FixImageView imageView, Drawable defaultDrawable, Bitmap.Config config, boolean resize, boolean isGif, int width, int height, int rotate) {
        if (isGif) {
            Glide
                    .with(context)
                    .load(path)
                    .apply(new RequestOptions()
                            .placeholder(defaultDrawable)
                            .error(defaultDrawable)
                            .override(width, height)
                            .transform(new RotateTransformation(rotate))
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(imageView);
        } else {
            Glide
                    .with(context)
                    .asBitmap()
                    .load(path)
                    .apply(new RequestOptions()
                            .placeholder(defaultDrawable)
                            .error(defaultDrawable)
                            .override(width, height)
                            .transform(new RotateTransformation(rotate))
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(imageView);
        }
    }
}
