package cn.finalteam.rxgalleryfinal;

import android.content.Context;

import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.utils.MediaType;

/**
 * Desction:配置信息
 * Author:pengjianbo
 * Date:16/5/7 下午3:58
 */
public class Configuration {

    protected Configuration() {
    }

    private boolean image = true;
    private Context context;
    private MediaType []filterMimes;
    private List<MediaBean> selectedList;

    private boolean radio;
    private int maxSize;
    private AbsImageLoader imageLoader;
    private PauseOnScrollListener pauseOnScrollListener;

    //==========UCrop START==========
    //是否隐藏裁剪页面底部控制栏,默认显示
    private boolean hideUCropBottomControls;
    //图片压缩质量,默认不压缩
    private int compressionQuality;
    //手势方式,默认all
    private @UCropActivity.GestureTypes int []gestures;
    //设置图片最大值,默认根据屏幕得出
    private int maxBitmapSize;
    //设置最大缩放值,默认10.f
    private int maxScaleMultiplier;
    //宽高比
    private int aspectRatioX;
    private int aspectRatioY;
    //等比缩放默认值索引,默认原图比例
    private int selectedByDefault;
    //等比缩放值表,默认1:1,3:4,原图比例,3:2,16:9
    private AspectRatio []aspectRatio;
    //是否允许改变裁剪大小
    private boolean freestyleCropEnabled;
    //是否显示裁剪框半透明椭圆浮层
    private boolean ovalDimmedLayer;
    //==========UCrop END==========

    public boolean isImage() {
        return image;
    }

    protected void setImage(boolean image) {
        this.image = image;
    }

    public Context getContext() {
        return context;
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    public MediaType[] getFilterMimes() {
        return filterMimes;
    }

    protected void setFilterMimes(MediaType[] filterMimes) {
        this.filterMimes = filterMimes;
    }

    public List<MediaBean> getSelectedList() {
        return selectedList;
    }

    protected void setSelectedList(List<MediaBean> selectedList) {
        this.selectedList = selectedList;
    }

    public boolean isRadio() {
        return radio;
    }

    protected void setRadio(boolean radio) {
        this.radio = radio;
    }

    public int getMaxSize() {
        return maxSize;
    }

    protected void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public AbsImageLoader getImageLoader() {
        return imageLoader;
    }

    protected void setImageLoader(AbsImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public PauseOnScrollListener getPauseOnScrollListener() {
        return pauseOnScrollListener;
    }

    protected void setPauseOnScrollListener(PauseOnScrollListener pauseOnScrollListener) {
        this.pauseOnScrollListener = pauseOnScrollListener;
    }

    public boolean isHideUCropBottomControls() {
        return hideUCropBottomControls;
    }

    public void setHideUCropBottomControls(boolean hideUCropBottomControls) {
        this.hideUCropBottomControls = hideUCropBottomControls;
    }

    public int getCompressionQuality() {
        return compressionQuality;
    }

    public void setCompressionQuality(int compressionQuality) {
        this.compressionQuality = compressionQuality;
    }

    public void setAllowedGestures(@UCropActivity.GestureTypes int []gestures) {
        this.gestures = gestures;
    }

    public int[] getAllowedGestures() {
        return gestures;
    }

    public int getMaxBitmapSize() {
        return maxBitmapSize;
    }

    public void setMaxBitmapSize(int maxBitmapSize) {
        this.maxBitmapSize = maxBitmapSize;
    }

    public int getMaxScaleMultiplier() {
        return maxScaleMultiplier;
    }

    public void setMaxScaleMultiplier(int maxScaleMultiplier) {
        this.maxScaleMultiplier = maxScaleMultiplier;
    }

    public int getAspectRatioX() {
        return aspectRatioX;
    }

    public void setAspectRatioX(int aspectRatioX) {
        this.aspectRatioX = aspectRatioX;
    }

    public int getAspectRatioY() {
        return aspectRatioY;
    }

    public void setAspectRatioY(int aspectRatioY) {
        this.aspectRatioY = aspectRatioY;
    }

    public void setAspectRatioOptions(int selectedByDefault, AspectRatio... aspectRatio) {
        this.selectedByDefault = selectedByDefault;
        this.aspectRatio = aspectRatio;
    }

    public int getSelectedByDefault() {
        return selectedByDefault;
    }

    public void setSelectedByDefault(int selectedByDefault) {
        this.selectedByDefault = selectedByDefault;
    }

    public AspectRatio[] getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(AspectRatio[] aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public boolean isFreestyleCropEnabled() {
        return freestyleCropEnabled;
    }

    public void setFreestyleCropEnabled(boolean freestyleCropEnabled) {
        this.freestyleCropEnabled = freestyleCropEnabled;
    }

    public boolean isOvalDimmedLayer() {
        return ovalDimmedLayer;
    }

    public void setOvalDimmedLayer(boolean ovalDimmedLayer) {
        this.ovalDimmedLayer = ovalDimmedLayer;
    }
}
