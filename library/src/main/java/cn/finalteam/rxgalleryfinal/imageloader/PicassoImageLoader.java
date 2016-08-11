package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;

import java.io.File;

import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/6/17 下午1:23
 */
public class PicassoImageLoader implements AbsImageLoader {

    @Override
    public void displayImage(Object context, String path, FixImageView imageView,
                             Drawable defaultDrawable, int width, int height) {
        Context ctx = (Context) context;
        Picasso.with(ctx)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .resize(width, height)
                .tag(context)
                .into(imageView);
    }
}
