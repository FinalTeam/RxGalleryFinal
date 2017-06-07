package cn.finalteam.rxgalleryfinal.sample.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import cn.finalteam.rxgalleryfinal.imageloader.AbsImageLoader;
import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * by y on 2017/6/7.
 */

public class SimpleImageLoader implements AbsImageLoader {


    @Override
    public void displayImage(Object context,
                             String path,
                             FixImageView imageView,
                             Drawable defaultDrawable,
                             Bitmap.Config config,
                             boolean resize,
                             int width,
                             int height,
                             int rotate) {

    }
}
