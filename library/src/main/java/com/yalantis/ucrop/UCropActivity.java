package com.yalantis.ucrop;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.ImageCropBean;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.component.BaseComponent;
import cn.finalteam.rxgalleryfinal.di.component.DaggerBaseComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.BaseModule;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.event.BaseResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.CloseRxMediaGridPageEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.BaseActivity;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).//b2b7690
 */

@SuppressWarnings("ConstantConditions")
public class UCropActivity extends BaseActivity {

    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;

    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;

    @IntDef({NONE, SCALE, ROTATE, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {

    }

    private static final int TABS_COUNT = 3;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;

    private int mActiveWidgetColor;
    private int mToolbarWidgetColor;

    private boolean mShowBottomControls;
    private boolean mShowLoader = true;

    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;
    private ViewGroup mWrapperStateAspectRatio, mWrapperStateRotate, mWrapperStateScale;
    private ViewGroup mLayoutAspectRatio, mLayoutRotate, mLayoutScale;
    private List<ViewGroup> mCropAspectRatioViews = new ArrayList<>();
    private TextView mTextViewRotateAngle, mTextViewScalePercent;
    private View mBlockingView;
    private Toolbar mToolbar;
    private ImageView mStateScaleImageView;
    private ImageView mStateRotateImageView;
    private ImageView mStateAspectRatioImageView;
    private TextView mTvToolbarTitle;
    private FrameLayout mUcropFrame;

    private Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = DEFAULT_COMPRESS_QUALITY;
    private int[] mAllowedGestures = new int[]{SCALE, ROTATE, ALL};
    private MediaBean mMediaBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_ucrop_activity_photobox);

        findViews();

        final Intent intent = getIntent();
        setupViews(intent);
        setImageData(intent);
        setInitialState();
        addBlockingView();

        mGestureCropImageView.setTransformImageListener(mImageListener);
    }

    @Override
    protected void setTheme(Resources.Theme theme) {
        mActiveWidgetColor = ThemeUtils.resolveColor(this, R.attr.gallery_color_active_widget, R.color.gallery_default_ucrop_color_widget_active);
        mToolbarWidgetColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_widget_color, R.color.gallery_default_color_toolbar_icon);

        mStateScaleImageView.setImageDrawable(new SelectedStateListDrawable(mStateScaleImageView.getDrawable(), mActiveWidgetColor));
        mStateRotateImageView.setImageDrawable(new SelectedStateListDrawable(mStateRotateImageView.getDrawable(), mActiveWidgetColor));
        mStateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable(mStateAspectRatioImageView.getDrawable(), mActiveWidgetColor));

        int statusBarColor = ThemeUtils.resolveColor(this, R.attr.gallery_color_statusbar, R.color.gallery_default_color_statusbar);
        ThemeUtils.setStatusBarColor(statusBarColor, getWindow());

        Drawable closeDrawable = ThemeUtils.resolveDrawable(this, R.attr.gallery_toolbar_close_image, R.drawable.gallery_default_toolbar_close_image);
        int closeColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_close_color, R.color.gallery_default_toolbar_widget_color);
        closeDrawable.setColorFilter(closeColor, PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(closeDrawable);

        float titleTextSize = ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_text_size, R.dimen.gallery_default_toolbar_text_size);
        mTvToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        int titleTextColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_text_color, R.color.gallery_default_toolbar_text_color);
        mTvToolbarTitle.setTextColor(titleTextColor);
        mTvToolbarTitle.setText(ThemeUtils.resolveString(this, R.attr.gallery_ucrop_title_text, R.string.gallery_ucrop_menu_crop));
        mToolbar.setTitleTextColor(titleTextColor);

        int toolbarBg = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_bg, R.color.gallery_default_color_toolbar_bg);
        mToolbar.setBackgroundColor(toolbarBg);
        int toolbarHeight = (int) ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_height, R.dimen.gallery_default_toolbar_height);
        mToolbar.setMinimumHeight(toolbarHeight);

        int gravity = ThemeUtils.resolveInteger(this, R.attr.gallery_toolbar_text_gravity, R.integer.gallery_default_toolbar_text_gravity);
        mTvToolbarTitle.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, gravity));

        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        mOverlayView.setDimmedColor(ThemeUtils.resolveColor(this, R.attr.gallery_ucrop_image_outer_frame_bg, R.color.gallery_default_ucrop_color_default_dimmed));
        mOverlayView.setCropFrameColor(ThemeUtils.resolveColor(this, R.attr.gallery_ucrop_crop_frame_color, R.color.gallery_default_ucrop_color_default_crop_frame));
        mOverlayView.setCropFrameStrokeWidth((int)ThemeUtils.resolveDimen(this, R.attr.gallery_ucrop_crop_frame_stroke_width, R.dimen.gallery_ucrop_default_crop_frame_stoke_width));
        mOverlayView.setCropGridColor(ThemeUtils.resolveColor(this, R.attr.gallery_ucrop_image_grid_color, R.color.gallery_default_ucrop_color_default_crop_grid));
        mOverlayView.setCropGridStrokeWidth((int)ThemeUtils.resolveDimen(this, R.attr.gallery_ucrop_image_grid_stroke_size, R.dimen.gallery_ucrop_default_crop_grid_stoke_width));
        mUcropFrame.setBackgroundColor(ThemeUtils.resolveColor(this, R.attr.gallery_ucrop_color_crop_background, R.color.gallery_default_ucrop_color_crop_background));
    }

    @Override
    protected void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent) {
        BaseComponent baseComponent = DaggerBaseComponent.builder()
                .rxGalleryFinalComponent(rxGalleryFinalComponent)
                .baseModule(new BaseModule())
                .build();
        baseComponent.inject(this);
    }

    private void findViews(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        ViewGroup photoBox = (ViewGroup) findViewById(R.id.ucrop_photobox);
        View.inflate(this, R.layout.gallery_ucrop_controls, photoBox);
        mWrapperStateAspectRatio = (ViewGroup) findViewById(R.id.state_aspect_ratio);
        mWrapperStateAspectRatio.setOnClickListener(mStateClickListener);
        mWrapperStateRotate = (ViewGroup) findViewById(R.id.state_rotate);
        mWrapperStateRotate.setOnClickListener(mStateClickListener);
        mWrapperStateScale = (ViewGroup) findViewById(R.id.state_scale);
        mWrapperStateScale.setOnClickListener(mStateClickListener);

        mLayoutAspectRatio = (ViewGroup) findViewById(R.id.layout_aspect_ratio);
        mLayoutRotate = (ViewGroup) findViewById(R.id.layout_rotate_wheel);
        mLayoutScale = (ViewGroup) findViewById(R.id.layout_scale_wheel);

        mUCropView = (UCropView) findViewById(R.id.ucrop);

        mStateScaleImageView = (ImageView) findViewById(R.id.image_view_state_scale);
        mStateRotateImageView = (ImageView) findViewById(R.id.image_view_state_rotate);
        mStateAspectRatioImageView = (ImageView) findViewById(R.id.image_view_state_aspect_ratio);

        mTextViewRotateAngle = ((TextView) findViewById(R.id.text_view_rotate));

        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();
        mUcropFrame = (FrameLayout) findViewById(R.id.ucrop_frame);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_ucrop_menu_activity, menu);

        // Change crop & loader menu icons color to match the rest of the UI colors

        MenuItem menuItemLoader = menu.findItem(R.id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            } catch (IllegalStateException e) {
                Logger.e(String.format("%s - %s", e.getMessage(), getString(R.string.gallery_ucrop_mutate_exception_hint)));
            }
            ((Animatable) menuItemLoader.getIcon()).start();
        }

        MenuItem menuItemCrop = menu.findItem(R.id.menu_crop);
        Drawable menuItemCropIcon = menuItemCrop.getIcon();
        if (menuItemCropIcon != null) {
            menuItemCropIcon.mutate();
            menuItemCropIcon.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            menuItemCrop.setIcon(menuItemCropIcon);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_crop) {
            cropAndSaveImage();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGestureCropImageView != null) {
            mGestureCropImageView.cancelAllAnimations();
        }
    }

    /**
     * This method extracts all data from the incoming intent and setups views properly.
     */
    private void setImageData(@NonNull Intent intent) {
        mMediaBean = intent.getParcelableExtra(UCrop.EXTRA_INPUT_BEAN);
        File file = new File(mMediaBean.getOriginalPath());
        Uri inputUri = Uri.fromFile(file);
        Uri outputUri = intent.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);
        processOptions(intent);

        if (inputUri != null && outputUri != null) {
            try {
                mGestureCropImageView.setImageUri(inputUri, outputUri);
            } catch (Exception e) {
                setResultError(e);
            }
        } else {
            setResultError(new NullPointerException(getString(R.string.gallery_ucrop_error_input_data_is_absent)));
        }
    }

    /**
     * This method extracts {@link com.yalantis.ucrop.UCrop.Options #optionsBundle} from incoming intent
     * and setups Activity, {@link OverlayView} and {@link CropImageView} properly.
     */
    @SuppressWarnings("deprecation")
    private void processOptions(@NonNull Intent intent) {
        // Bitmap compression options
        String compressionFormatName = intent.getStringExtra(UCrop.Options.EXTRA_COMPRESSION_FORMAT_NAME);
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf(compressionFormatName);
        }
        mCompressFormat = (compressFormat == null) ? DEFAULT_COMPRESS_FORMAT : compressFormat;

        mCompressQuality = intent.getIntExtra(UCrop.Options.EXTRA_COMPRESSION_QUALITY, UCropActivity.DEFAULT_COMPRESS_QUALITY);

        // Gestures options
        int[] allowedGestures = intent.getIntArrayExtra(UCrop.Options.EXTRA_ALLOWED_GESTURES);
        if (allowedGestures != null && allowedGestures.length == TABS_COUNT) {
            mAllowedGestures = allowedGestures;
        }

        // Crop image view options
        mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(UCrop.Options.EXTRA_MAX_BITMAP_SIZE, CropImageView.DEFAULT_MAX_BITMAP_SIZE));
        mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(UCrop.Options.EXTRA_MAX_SCALE_MULTIPLIER, CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER));
        mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));

        // Overlay view options
//        boolean freeStyleCrop = ThemeUtils.resolveBoolean(this, R.attr.gallery_ucrop_free_style_crop, R.bool.gallery_default_ucrop_free_style_crop);
        mOverlayView.setFreestyleCropEnabled(intent.getBooleanExtra(UCrop.Options.EXTRA_FREE_STYLE_CROP, OverlayView.DEFAULT_FREESTYLE_CROP_ENABLED));
//        boolean ovalDimmedLayer = ThemeUtils.resolveBoolean(this, R.attr.gallery_ucrop_oval_dimmed_layer_enabled, R.bool.gallery_default_ucrop_oval_dimmed_layer_enabled);
//        mOverlayView.setOvalDimmedLayer(ovalDimmedLayer);
        mOverlayView.setOvalDimmedLayer(intent.getBooleanExtra(UCrop.Options.EXTRA_OVAL_DIMMED_LAYER, OverlayView.DEFAULT_OVAL_DIMMED_LAYER));
        boolean showCropFrame = ThemeUtils.resolveBoolean(this, R.attr.gallery_ucrop_show_crop_frame, R.bool.gallery_default_ucrop_show_crop_frame);
        mOverlayView.setShowCropFrame(showCropFrame);
        boolean showCropGrid = ThemeUtils.resolveBoolean(this, R.attr.gallery_ucrop_show_crop_grid, R.bool.gallery_default_ucrop_show_crop_grid);
        mOverlayView.setShowCropGrid(showCropGrid);
        int gridRowCount = ThemeUtils.resolveInteger(this, R.attr.gallery_ucrop_grid_column_count, R.integer.gallery_default_ucrop_show_crop_grid_row_count);
        mOverlayView.setCropGridRowCount(gridRowCount);
        int gridColumnCount = ThemeUtils.resolveInteger(this, R.attr.gallery_ucrop_grid_column_count, R.integer.gallery_default_ucrop_show_crop_grid_column_count);
        mOverlayView.setCropGridColumnCount(gridColumnCount);

        // Aspect ratio options宽高比
        float aspectRatioX = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_X, 0);
        float aspectRatioY = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_Y, 0);

        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);

        if (aspectRatioX > 0 && aspectRatioY > 0) {
            if (mWrapperStateAspectRatio != null) {
                mWrapperStateAspectRatio.setVisibility(View.GONE);
            }
            mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
        } else if (aspectRatioList != null && aspectRationSelectedByDefault < aspectRatioList.size()) {
            mGestureCropImageView.setTargetAspectRatio(aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioX() /
                    aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioY());
        } else {
            mGestureCropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
        }

        // Result bitmap max size options
        int maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0);
        int maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0);

        if (maxSizeX > 0 && maxSizeY > 0) {
            mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }

    private void setupViews(@NonNull Intent intent) {
        mShowBottomControls = !intent.getBooleanExtra(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS, false);
        if (mShowBottomControls) {
            setupAspectRatioWidget(intent);
            setupRotateWidget();
            setupScaleWidget();
        }
    }

    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {
            setAngleText(currentAngle);
        }

        @Override
        public void onScale(float currentScale) {
            setScaleText(currentScale);
        }

        @Override
        public void onLoadComplete() {
            mUCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
            mBlockingView.setClickable(false);
            mShowLoader = false;
            supportInvalidateOptionsMenu();
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            setResultError(e);
        }

    };

    private void setupAspectRatioWidget(@NonNull Intent intent) {

        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);

        if (aspectRatioList == null || aspectRatioList.isEmpty()) {
            aspectRationSelectedByDefault = 2;

            aspectRatioList = new ArrayList<>();
            aspectRatioList.add(new AspectRatio(null, 1, 1));
            aspectRatioList.add(new AspectRatio(null, 3, 4));
            aspectRatioList.add(new AspectRatio(getString(R.string.gallery_ucrop_label_original).toUpperCase(),
                    CropImageView.SOURCE_IMAGE_ASPECT_RATIO, CropImageView.SOURCE_IMAGE_ASPECT_RATIO));
            aspectRatioList.add(new AspectRatio(null, 3, 2));
            aspectRatioList.add(new AspectRatio(null, 16, 9));
        }

        LinearLayout wrapperAspectRatioList = (LinearLayout) findViewById(R.id.layout_aspect_ratio);

        FrameLayout wrapperAspectRatio;
        AspectRatioTextView aspectRatioTextView;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        for (AspectRatio aspectRatio : aspectRatioList) {
            wrapperAspectRatio = (FrameLayout) getLayoutInflater().inflate(R.layout.gallery_ucrop_aspect_ratio, null);
            wrapperAspectRatio.setLayoutParams(lp);
            aspectRatioTextView = ((AspectRatioTextView) wrapperAspectRatio.getChildAt(0));
            aspectRatioTextView.setActiveColor(mActiveWidgetColor);
            aspectRatioTextView.setAspectRatio(aspectRatio);

            wrapperAspectRatioList.addView(wrapperAspectRatio);
            mCropAspectRatioViews.add(wrapperAspectRatio);
        }

        mCropAspectRatioViews.get(aspectRationSelectedByDefault).setSelected(true);

        for (ViewGroup cropAspectRatioView : mCropAspectRatioViews) {
            cropAspectRatioView.setOnClickListener(v -> {
                mGestureCropImageView.setTargetAspectRatio(
                        ((AspectRatioTextView) ((ViewGroup) v).getChildAt(0)).getAspectRatio(v.isSelected()));
                mGestureCropImageView.setImageToWrapCropBounds();
                if (!v.isSelected()) {
                    for (ViewGroup cropAspectRatioView1 : mCropAspectRatioViews) {
                        cropAspectRatioView1.setSelected(cropAspectRatioView1 == v);
                    }
                }
            });
        }
    }

    private void setupRotateWidget() {

        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel))
                .setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
                    @Override
                    public void onScroll(float delta, float totalDistance) {
                        mGestureCropImageView.postRotate(delta / ROTATE_WIDGET_SENSITIVITY_COEFFICIENT);
                    }

                    @Override
                    public void onScrollEnd() {
                        mGestureCropImageView.setImageToWrapCropBounds();
                    }

                    @Override
                    public void onScrollStart() {
                        mGestureCropImageView.cancelAllAnimations();
                    }
                });

        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setMiddleLineColor(mActiveWidgetColor);


        findViewById(R.id.wrapper_reset_rotate).setOnClickListener(v -> resetRotation());
        findViewById(R.id.wrapper_rotate_by_angle).setOnClickListener(v -> rotateByAngle(90));
    }

    private void setupScaleWidget() {
        mTextViewScalePercent = ((TextView) findViewById(R.id.text_view_scale));
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel))
                .setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
                    @Override
                    public void onScroll(float delta, float totalDistance) {
                        if (delta > 0) {
                            mGestureCropImageView.zoomInImage(mGestureCropImageView.getCurrentScale()
                                    + delta * ((mGestureCropImageView.getMaxScale() - mGestureCropImageView.getMinScale()) / SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                        } else {
                            mGestureCropImageView.zoomOutImage(mGestureCropImageView.getCurrentScale()
                                    + delta * ((mGestureCropImageView.getMaxScale() - mGestureCropImageView.getMinScale()) / SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                        }
                    }

                    @Override
                    public void onScrollEnd() {
                        mGestureCropImageView.setImageToWrapCropBounds();
                    }

                    @Override
                    public void onScrollStart() {
                        mGestureCropImageView.cancelAllAnimations();
                    }
                });
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setMiddleLineColor(mActiveWidgetColor);
    }

    private void setAngleText(float angle) {
        if (mTextViewRotateAngle != null) {
            mTextViewRotateAngle.setText(String.format(Locale.getDefault(), "%.1f°", angle));
        }
    }

    private void setScaleText(float scale) {
        if (mTextViewScalePercent != null) {
            mTextViewScalePercent.setText(String.format(Locale.getDefault(), "%d%%", (int) (scale * 100)));
        }
    }

    private void resetRotation() {
        mGestureCropImageView.postRotate(-mGestureCropImageView.getCurrentAngle());
        mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int angle) {
        mGestureCropImageView.postRotate(angle);
        mGestureCropImageView.setImageToWrapCropBounds();
    }

    private final View.OnClickListener mStateClickListener = v -> {
        if (!v.isSelected()) {
            setWidgetState(v.getId());
        }
    };

    private void setInitialState() {
        if (mShowBottomControls) {
            if (mWrapperStateAspectRatio.getVisibility() == View.VISIBLE) {
                setWidgetState(R.id.state_aspect_ratio);
            } else {
                setWidgetState(R.id.state_scale);
            }
        } else {
            mGestureCropImageView.setScaleEnabled(true);
            mGestureCropImageView.setRotateEnabled(true);
        }
    }

    private void setWidgetState(@IdRes int stateViewId) {
        if (!mShowBottomControls) return;

        mWrapperStateAspectRatio.setSelected(stateViewId == R.id.state_aspect_ratio);
        mWrapperStateRotate.setSelected(stateViewId == R.id.state_rotate);
        mWrapperStateScale.setSelected(stateViewId == R.id.state_scale);

        mLayoutAspectRatio.setVisibility(stateViewId == R.id.state_aspect_ratio ? View.VISIBLE : View.GONE);
        mLayoutRotate.setVisibility(stateViewId == R.id.state_rotate ? View.VISIBLE : View.GONE);
        mLayoutScale.setVisibility(stateViewId == R.id.state_scale ? View.VISIBLE : View.GONE);

        if (stateViewId == R.id.state_scale) {
            setAllowedGestures(0);
        } else if (stateViewId == R.id.state_rotate) {
            setAllowedGestures(1);
        } else {
            setAllowedGestures(2);
        }
    }

    private void setAllowedGestures(int tab) {
        mGestureCropImageView.setScaleEnabled(mAllowedGestures[tab] == ALL || mAllowedGestures[tab] == SCALE);
        mGestureCropImageView.setRotateEnabled(mAllowedGestures[tab] == ALL || mAllowedGestures[tab] == ROTATE);
    }

    /**
     * Adds view that covers everything below the Toolbar.
     * When it's clickable - user won't be able to click/touch anything below the Toolbar.
     * Need to block user input while loading and cropping an image.
     */
    private void addBlockingView() {
        if (mBlockingView == null) {
            mBlockingView = new View(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
            mBlockingView.setLayoutParams(lp);
            mBlockingView.setClickable(true);
        }

        ((RelativeLayout) findViewById(R.id.ucrop_photobox)).addView(mBlockingView);
    }

    protected void cropAndSaveImage() {
        mBlockingView.setClickable(true);
        mShowLoader = true;
        supportInvalidateOptionsMenu();

        mGestureCropImageView.cropAndSaveImage(mCompressFormat, mCompressQuality, new BitmapCropCallback() {

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri) {
                setResultUri(resultUri, mGestureCropImageView.getTargetAspectRatio());
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                setResultError(t);
            }
        });
    }

    protected void setResultUri(Uri uri, float resultAspectRatio) {
        ImageCropBean bean = new ImageCropBean();
        bean.copyMediaBean(mMediaBean);
        bean.setCropPath(uri.getPath());
        bean.setAspectRatio(resultAspectRatio);
        BaseResultEvent event = new ImageRadioResultEvent(bean);
        RxBus.getDefault().post(event);
        RxBus.getDefault().post(new CloseRxMediaGridPageEvent());
        finish();
    }

    protected void setResultError(Throwable throwable) {
        Logger.e(String.format("Image crop error:%s", throwable.getMessage()));
        RxBus.getDefault().post(new CloseRxMediaGridPageEvent());
        finish();
    }

}
