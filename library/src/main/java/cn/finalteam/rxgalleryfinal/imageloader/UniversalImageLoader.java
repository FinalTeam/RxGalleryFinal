package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by pengjianbo  Dujinyang on 2016/8/13 0013.
 */
public class UniversalImageLoader implements AbsImageLoader {

    private DisplayImageOptions displayImageOptions;

    @Override
    public void displayImage(Context context, String path, FixImageView imageView, Drawable defaultDrawable, Bitmap.Config config, boolean resize, boolean isGif, int width, int height, int rotate) {
        if (displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(false)
                    .cacheInMemory(true)
                    .bitmapConfig(config)
                    .showImageOnFail(defaultDrawable)
                    .showImageOnLoading(defaultDrawable)
                    .showImageForEmptyUri(defaultDrawable)
                    .build();
        }
        ImageSize imageSize = null;
        if (resize) {
            imageSize = new ImageSize(width, height);
        }
        ImageLoader.getInstance().displayImage("file://" + path, new ImageViewAware(imageView), displayImageOptions, imageSize, null, null);
    }
}
