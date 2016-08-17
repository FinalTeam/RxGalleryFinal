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
 * Author:pengjianbo
 * Date:16/6/17 下午1:23
 */
public class PicassoImageLoader implements AbsImageLoader {

    @Override
    public void displayImage(Object context, String path, FixImageView imageView,
                             Drawable defaultDrawable, Bitmap.Config config, boolean resize, int width, int height, int rotate) {
        Context ctx = (Context) context;
        RequestCreator creator = Picasso.with(ctx)
                .load(new File(path))
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .rotate(rotate)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .config(config)
                .tag(context);
        if(resize){
            creator = creator.resize(width, height);
        }
       creator.into(imageView);
    }
}
