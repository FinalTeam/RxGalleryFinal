package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
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

    public static void setImageSmall(String url, SimpleDraweeView simpleDraweeView) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(simpleDraweeView.getLayoutParams().width,
                        simpleDraweeView.getLayoutParams().height))
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller);
    }

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
        if (resize) {
            creator = creator.resize(width, height)
                    .centerCrop();
        }
        creator.into(imageView);
    }
}
