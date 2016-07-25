package cn.finalteam.rxgalleryfinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.component.DaggerRxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.RxGalleryFinalModule;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.utils.MediaType;
import cn.finalteam.rxgalleryfinal.utils.StorageUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 下午4:20
 */
public class RxGalleryFinal {

    private RxGalleryFinalComponent mRxGalleryFinalComponent;

    private RxGalleryFinal(){}

    Configuration configuration = new Configuration();
    static RxGalleryFinal instance;

    public static RxGalleryFinal with(@NonNull Context context) {
        instance = new RxGalleryFinal();
        instance.configuration.setContext(context);
        return instance;
    }

    public RxGalleryFinal image(){
        configuration.setImage(true);
        return this;
    }

    public RxGalleryFinal video() {
        configuration.setImage(false);
        return this;
    }

    public RxGalleryFinal filterMime(@NonNull MediaType ...mediaTypes) {
        configuration.setFilterMimes(mediaTypes);
        return this;
    }

    public RxGalleryFinal radio(){
        configuration.setRadio(true);
        return this;
    }

    public RxGalleryFinal multiple() {
        configuration.setRadio(false);
        return this;
    }

    public RxGalleryFinal maxSize(@IntRange(from = 1) int maxSize){
        configuration.setMaxSize(maxSize);
        return this;
    }

    public RxGalleryFinal selectedList(List<MediaBean> list) {
        configuration.setSelectedList(list);
        return this;
    }

    public RxGalleryFinal imageLoader(ImageLoaderType imageLoaderType) {
        configuration.setImageLoaderType(imageLoaderType);
        return this;
    }

    public RxGalleryFinal pauseOnScrollListener(PauseOnScrollListener pauseOnScrollListener) {
        configuration.setPauseOnScrollListener(pauseOnScrollListener);
        return this;
    }

    /**
     * set to true to hide the bottom controls (shown by default)
     * @param hide
     * @return
     */
    public RxGalleryFinal uCropHideBottomControls(boolean hide) {
        configuration.setHideBottomControls(hide);
        return this;
    }

    /**
     * Set compression quality [0-100] that will be used to save resulting Bitmap.
     * @param compressQuality
     */
    public RxGalleryFinal uCropropCompressionQuality(@IntRange(from = 0) int compressQuality) {
        configuration.setCompressionQuality(compressQuality);
        return this;
    }

    /**
     * Choose what set of gestures will be enabled on each tab - if any.
     * @param tabScale
     * @param tabRotate
     * @param tabAspectRatio
     */
    public RxGalleryFinal uCropAllowedGestures(@UCropActivity.GestureTypes int tabScale,
                                   @UCropActivity.GestureTypes int tabRotate,
                                   @UCropActivity.GestureTypes int tabAspectRatio) {
        configuration.setAllowedGestures(new int[]{tabScale, tabRotate, tabAspectRatio});
        return this;
    }

    /**
     * Setter for max size for both width and height of bitmap that will be decoded from an input Uri and used in the view.
     *
     * @param maxBitmapSize - size in pixels
     */
    public RxGalleryFinal uCropMaxBitmapSize(@IntRange(from = 100) int maxBitmapSize) {
        configuration.setMaxBitmapSize(maxBitmapSize);
        return this;
    }

    /**
     * This method sets multiplier that is used to calculate max image scale from min image scale.
     *
     * @param maxScaleMultiplier - (minScale * maxScaleMultiplier) = maxScale
     */
    public RxGalleryFinal uCropMaxScaleMultiplier(@FloatRange(from = 1.0, fromInclusive = false) float maxScaleMultiplier) {
        configuration.setMaxScaleMultiplier(maxScaleMultiplier);
        return this;
    }

    /**
     * Set an aspect ratio for crop bounds.
     * User won't see the menu with other ratios options.
     *
     * @param x aspect ratio X
     * @param y aspect ratio Y
     */
    public RxGalleryFinal uCropWithAspectRatio(float x, float y) {
        configuration.setAspectRatioX(x);
        configuration.setAspectRatioY(y);
        return this;
    }

    /**
     * Set an aspect ratio for crop bounds that is evaluated from source image width and height.
     * User won't see the menu with other ratios options.
     */
    public RxGalleryFinal uCropUseSourceImageAspectRatio() {
        configuration.setAspectRatioX(0);
        configuration.setAspectRatioY(0);
        return this;
    }

    /**
     * Pass an ordered list of desired aspect ratios that should be available for a user.
     *
     * @param selectedByDefault - index of aspect ratio option that is selected by default (starts with 0).
     * @param aspectRatio       - list of aspect ratio options that are available to user
     */
    public RxGalleryFinal uCropAspectRatioOptions(int selectedByDefault, AspectRatio... aspectRatio) {
        configuration.setSelectedByDefault(selectedByDefault);
        configuration.setAspectRatio(aspectRatio);
        return this;
    }

    /**
     * set to true to let user resize crop bounds (disabled by default)
     * @param enabled
     */
    public RxGalleryFinal uCropFreeStyleCropEnabled(boolean enabled) {
        configuration.setFreestyleCropEnabled(enabled);
        return this;
    }

    /**
     * set it to true if you want dimmed layer to have an oval inside
     * @param isOval
     */
    public RxGalleryFinal uCropOvalDimmedLayer(boolean isOval) {
        configuration.setOvalDimmedLayer(isOval);
        return this;
    }

    public void openGallery(){
        execute();
    }

    private void execute() {
        Context context = configuration.getContext();
        if(context == null) {
            return;
        }
        if(!StorageUtils.existSDcard()){
            Toast.makeText(context, "没有找到SD卡", Toast.LENGTH_SHORT).show();
            return;
        }

        mRxGalleryFinalComponent = DaggerRxGalleryFinalComponent.builder()
                .rxGalleryFinalModule(new RxGalleryFinalModule(configuration))
                .build();

        mRxGalleryFinalComponent.inject(this);

        Intent intent = new Intent(context, MediaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static RxGalleryFinalComponent getRxGalleryFinalComponent() {
        if(instance == null) {
            return null;
        }
        return instance.mRxGalleryFinalComponent;
    }
}
