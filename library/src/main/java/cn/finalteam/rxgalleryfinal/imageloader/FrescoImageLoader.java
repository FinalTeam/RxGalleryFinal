package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.MotionEvent;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by pengjianbo on 2016/8/13 0013.
 */
public class FrescoImageLoader implements AbsImageLoader {

    private DraweeHolder<GenericDraweeHierarchy> draweeHolder;

    private void init(Context ctx, Drawable defaultDrawable) {
        if(draweeHolder == null) {
            Resources resources = ctx.getResources();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                    .setPlaceholderImage(defaultDrawable)
                    .setFailureImage(defaultDrawable)
                    .build();
            draweeHolder = DraweeHolder.create(hierarchy, ctx);
        }
    }

    @Override
    public void displayImage(Object context, String path, FixImageView imageView, Drawable defaultDrawable, Bitmap.Config config, boolean resize, int width, int height, int rotate) {
        Context ctx = (Context) context;
        init(ctx, defaultDrawable);

        imageView.setOnImageViewListener(new FixImageView.OnImageViewListener() {
            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }

            @Override
            public boolean verifyDrawable(Drawable dr) {
                if (dr == draweeHolder.getHierarchy().getTopLevelDrawable()) {
                    return true;
                }
                return false;
            }

            @Override
            public void onDraw(Canvas canvas) {
                Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                if (drawable == null) {
                    imageView.setImageDrawable(defaultDrawable);
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return draweeHolder.onTouchEvent(event);
            }
        });
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true);
        if(resize){
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(request)
                .build();
        draweeHolder.setController(controller);
    }
}
