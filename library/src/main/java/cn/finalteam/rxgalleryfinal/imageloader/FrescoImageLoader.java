package cn.finalteam.rxgalleryfinal.imageloader;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by pengjianbo on 2016/8/13 0013.
 */
public class FrescoImageLoader implements AbsImageLoader {

    @Override
    public void displayImage(Object context, String path, FixImageView imageView, Drawable defaultDrawable, boolean resize, int width, int height, int rotate) {
        Uri uri = Uri.fromFile(new File(path));
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true);
        if(resize){
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
    }
}
