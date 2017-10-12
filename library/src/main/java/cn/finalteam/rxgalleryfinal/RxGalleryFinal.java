package cn.finalteam.rxgalleryfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.exception.UnknownImageLoaderTypeException;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.BaseResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.utils.StorageUtils;
import rx.Subscription;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 下午4:20
 */
public class RxGalleryFinal {

    private RxGalleryFinal(){}

    Configuration configuration = new Configuration();
    static RxGalleryFinal instance;
    RxBusResultSubscriber<BaseResultEvent> rxBusResultSubscriber;

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

//    public RxGalleryFinal filterMime(MediaType ...mediaTypes) {
//        configuration.setFilterMimes(mediaTypes);
//        return this;
//    }

    public RxGalleryFinal radio(){
        configuration.setRadio(true);
        return this;
    }

    public RxGalleryFinal multiple() {
        configuration.setRadio(false);
        return this;
    }

    public RxGalleryFinal crop(){
        configuration.setCrop(true);
        return this;
    }

    public RxGalleryFinal maxSize(@IntRange(from = 1) int maxSize){
        configuration.setMaxSize(maxSize);
        return this;
    }

    public RxGalleryFinal selected(@NonNull List<MediaBean> list) {
        configuration.setSelectedList(list);
        return this;
    }

    public RxGalleryFinal imageConfig(@NonNull Bitmap.Config config) {
        int c = 3;
        switch (config){
            case ALPHA_8:
                c = 1;
                break;
            case ARGB_4444:
                c = 2;
                break;
            case ARGB_8888:
                c = 3;
                break;
            case RGB_565:
                c = 4;
                break;
        }

        configuration.setImageConfig(c);

        return this;
    }

    public RxGalleryFinal imageLoader(@NonNull ImageLoaderType imageLoaderType) {
        int type = 0;
        if(imageLoaderType == ImageLoaderType.PICASSO){
            type = 1;
        } else if(imageLoaderType == ImageLoaderType.GLIDE){
            type = 2;
        } else if(imageLoaderType == ImageLoaderType.FRESCO){
            type = 3;
        } else if(imageLoaderType == ImageLoaderType.UNIVERSAL){
            type = 4;
        }
        configuration.setImageLoaderType(type);
        return this;
    }

    /**
     * 隐藏相机
     * @return
     */
    public RxGalleryFinal hideCamera() {
        configuration.setHideCamera(true);
        return this;
    }

    /**
     * 直接打开相机
     * @return
     */
    public RxGalleryFinal openCameraOnStart() {
        configuration.setOpenCameraOnStart(true);
        return this;
    }

    /**
     * 拍摄后，直接返回图片
     * @return
     */
    public RxGalleryFinal returnAfterShot() {
        configuration.setReturnAfterShot(true);
        return this;
    }
    /**
     * set to true to hide the bottom controls (shown by default)
     * @param hide
     * @return
     */
    public RxGalleryFinal cropHideBottomControls(boolean hide) {
        configuration.setHideBottomControls(hide);
        return this;
    }

    /**
     * Set compression quality [0-100] that will be used to save resulting Bitmap.
     * @param compressQuality
     */
    public RxGalleryFinal cropropCompressionQuality(@IntRange(from = 0) int compressQuality) {
        configuration.setCompressionQuality(compressQuality);
        return this;
    }

    /**
     * Choose what set of gestures will be enabled on each tab - if any.
     * @param tabScale
     * @param tabRotate
     * @param tabAspectRatio
     */
    public RxGalleryFinal cropAllowedGestures(@UCropActivity.GestureTypes int tabScale,
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
    public RxGalleryFinal cropMaxBitmapSize(@IntRange(from = 100) int maxBitmapSize) {
        configuration.setMaxBitmapSize(maxBitmapSize);
        return this;
    }

    /**
     * This method sets multiplier that is used to calculate max image scale from min image scale.
     *
     * @param maxScaleMultiplier - (minScale * maxScaleMultiplier) = maxScale
     */
    public RxGalleryFinal cropMaxScaleMultiplier(@FloatRange(from = 1.0, fromInclusive = false) float maxScaleMultiplier) {
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
    public RxGalleryFinal cropWithAspectRatio(float x, float y) {
        configuration.setAspectRatioX(x);
        configuration.setAspectRatioY(y);
        return this;
    }

    /**
     * Set an aspect ratio for crop bounds that is evaluated from source image width and height.
     * User won't see the menu with other ratios options.
     */
    public RxGalleryFinal cropUseSourceImageAspectRatio() {
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
    public RxGalleryFinal cropAspectRatioOptions(int selectedByDefault, AspectRatio... aspectRatio) {
        configuration.setSelectedByDefault(selectedByDefault);
        configuration.setAspectRatio(aspectRatio);
        return this;
    }

    /**
     * set to true to let user resize crop bounds (disabled by default)
     * @param enabled
     */
    public RxGalleryFinal cropFreeStyleCropEnabled(boolean enabled) {
        configuration.setFreestyleCropEnabled(enabled);
        return this;
    }

    /**
     * set it to true if you want dimmed layer to have an oval inside
     * @param isOval
     */
    public RxGalleryFinal cropOvalDimmedLayer(boolean isOval) {
        configuration.setOvalDimmedLayer(isOval);
        return this;
    }

    /**
     * 设置裁剪结果最大宽度和高度
     * @param width
     * @param height
     */
    public RxGalleryFinal cropMaxResultSize(@IntRange(from = 100) int width, @IntRange(from = 100) int height) {
        configuration.setMaxResultSize(width, height);
        return this;
    }

    /**
     * 设置显示标题
     * @return
     */
    public RxGalleryFinal setTitle(String title) {
        configuration.setTitle(title);
        return this;
    }
    /**
     * 设置回调
     * @param rxBusResultSubscriber
     * @return
     */
    public RxGalleryFinal subscribe(@NonNull RxBusResultSubscriber<? extends BaseResultEvent> rxBusResultSubscriber) {
        this.rxBusResultSubscriber = (RxBusResultSubscriber<BaseResultEvent>) rxBusResultSubscriber;
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

        if(configuration.getImageLoader() == null) {
            throw new UnknownImageLoaderTypeException();
        }

        if(rxBusResultSubscriber == null){
            return;
        }

        Subscription subscription;
        if(configuration.isRadio()) {
            subscription = RxBus.getDefault()
                    .toObservable(ImageRadioResultEvent.class)
                    .subscribe(rxBusResultSubscriber);
        } else {
            subscription = RxBus.getDefault()
                    .toObservable(ImageMultipleResultEvent.class)
                    .subscribe(rxBusResultSubscriber);
        }
        RxBus.getDefault().add(subscription);

        Intent intent = new Intent(context, MediaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MediaActivity.EXTRA_CONFIGURATION, configuration);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
