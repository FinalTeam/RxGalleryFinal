package cn.finalteam.rxgalleryfinal.ui.activity;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.component.ActivityFragmentComponent;
import cn.finalteam.rxgalleryfinal.di.component.DaggerActivityFragmentComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.ActivityFragmentModule;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaCheckChangeEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaViewPagerChangedEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPageFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPreviewFragmentEvent;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaGridFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPageFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPreviewFragment;
import cn.finalteam.rxgalleryfinal.utils.OsCompat;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;
import cn.finalteam.rxgalleryfinal.view.ActivityFragmentView;
import rx.Subscription;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:01
 */
public class MediaActivity extends BaseActivity implements ActivityFragmentView {

    @Inject
    Configuration mConfiguration;
    @Inject
    MediaGridFragment mMediaGridFragment;
    @Inject
    MediaPageFragment mMediaPageFragment;
    @Inject
    MediaPreviewFragment mMediaPreviewFragment;

    private Toolbar mToolbar;
    private TextView mTvToolbarTitle;
    private TextView mTvOverAction;

    private List<MediaBean> mCheckedList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity_media);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");

        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mTvOverAction = (TextView) findViewById(R.id.tv_over_action);
        if(!mConfiguration.isRadio()) {
            mTvOverAction.setOnClickListener(view -> {

            });
            mTvOverAction.setVisibility(View.VISIBLE);
        } else {
            mTvOverAction.setVisibility(View.GONE);
        }
        mCheckedList = new ArrayList<>();

        showMediaGridFragment();
        subscribeEvent();

    }

    @Override
    protected void setTheme(Resources.Theme theme) {
        Drawable closeDrawable = ThemeUtils.resolveDrawable(this, R.attr.gallery_toolbar_close_image, R.drawable.gallery_default_toolbar_close_image);
        int closeColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_close_color, R.color.gallery_default_toolbar_widget_color);
        closeDrawable.setColorFilter(closeColor, PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(closeDrawable);

        int overButtonBg = ThemeUtils.resolveDrawableRes(this, R.attr.gallery_toolbar_over_button_bg);
        if(overButtonBg != 0) {
            mTvOverAction.setBackgroundResource(overButtonBg);
        } else {
            OsCompat.setBackgroundDrawableCompat(mTvOverAction, createDefaultOverButtonBgDrawable());
        }

        float overTextSize = ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_over_button_text_size, R.dimen.gallery_default_toolbar_over_button_text_size);
        mTvOverAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, overTextSize);

        int overTextColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_over_button_text_color, R.color.gallery_default_toolbar_over_button_text_color);
        mTvOverAction.setTextColor(overTextColor);

        float titleTextSize = ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_text_size, R.dimen.gallery_default_toolbar_text_size);
        mTvToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);

        int titleTextColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_text_color, R.color.gallery_default_toolbar_text_color);
        mTvToolbarTitle.setTextColor(titleTextColor);

        int gravity = ThemeUtils.resolveInteger(this, R.attr.gallery_toolbar_text_gravity, R.integer.gallery_default_toolbar_text_gravity);
        mTvToolbarTitle.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, gravity));

        int toolbarBg = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_bg, R.color.gallery_default_color_toolbar_bg);
        mToolbar.setBackgroundColor(toolbarBg);

        int toolbarHeight = (int) ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_height, R.dimen.gallery_default_toolbar_height);
        mToolbar.setMinimumHeight(toolbarHeight);

        int statusBarColor = ThemeUtils.resolveColor(this, R.attr.gallery_color_statusbar, R.color.gallery_default_color_statusbar);
        ThemeUtils.setStatusBarColor(statusBarColor, getWindow());

        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backAction();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMediaGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mMediaGridFragment)
                .hide(mMediaPreviewFragment)
                .hide(mMediaPageFragment)
                .show(mMediaGridFragment)
                .commit();

        if(mConfiguration.isImage()) {
            mTvToolbarTitle.setText("图片");
        } else {
            mTvToolbarTitle.setText("视频");
        }
    }

    @Override
    public void showMediaPageFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!mMediaPageFragment.isAdded()){
            ft.add(R.id.fragment_container, mMediaPageFragment);
        }
        ft.hide(mMediaGridFragment)
                .hide(mMediaPreviewFragment)
                .show(mMediaPageFragment)
                .commit();
    }

    @Override
    public void showMediaPreviewFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!mMediaPreviewFragment.isAdded()){
            ft.add(R.id.fragment_container, mMediaPreviewFragment);
        }
        ft.hide(mMediaGridFragment)
                .hide(mMediaPageFragment)
                .show(mMediaPreviewFragment)
                .commit();

        mTvToolbarTitle.setText(String.format(Locale.CHINA, "预览(%d/%d)", 1, mCheckedList.size()));
    }

    @Override
    protected void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent) {
        ActivityFragmentComponent activityFragmentComponent = DaggerActivityFragmentComponent.builder()
            .rxGalleryFinalComponent(rxGalleryFinalComponent)
            .activityFragmentModule(new ActivityFragmentModule())
            .build();
        activityFragmentComponent.inject(this);
    }


    private void subscribeEvent() {
        Subscription subscriptionOpenMediaPreviewEvent = RxBus.getDefault().toObservable(OpenMediaPreviewFragmentEvent.class)
                .map(mediaPreviewEvent -> mediaPreviewEvent)
                .subscribe(new RxBusSubscriber<OpenMediaPreviewFragmentEvent>() {
                    @Override
                    protected void onEvent(OpenMediaPreviewFragmentEvent openMediaPreviewFragmentEvent) {
                        showMediaPreviewFragment();
                    }
                });

        RxBus.getDefault().add(subscriptionOpenMediaPreviewEvent);

        Subscription subscriptionMediaCheckChangeEvent = RxBus.getDefault().toObservable(MediaCheckChangeEvent.class)
                .map(mediaCheckChangeEvent -> mediaCheckChangeEvent)
                .subscribe(new RxBusSubscriber<MediaCheckChangeEvent>() {
                    @Override
                    protected void onEvent(MediaCheckChangeEvent mediaCheckChangeEvent) {
                        MediaBean mediaBean = mediaCheckChangeEvent.getMediaBean();
                        if(mCheckedList.contains(mediaBean)) {
                            mCheckedList.remove(mediaBean);
                        } else {
                            mCheckedList.add(mediaBean);
                        }

                        if(mCheckedList.size() > 0){
                            String text = getResources().getString(R.string.gallery_over_button_text_checked, mCheckedList.size(), mConfiguration.getMaxSize());
                            mTvOverAction.setText(text);
                            mTvOverAction.setEnabled(true);
                        } else {
                            mTvOverAction.setText(R.string.gallery_over_button_text);
                            mTvOverAction.setEnabled(false);
                        }
                    }
                });
        RxBus.getDefault().add(subscriptionMediaCheckChangeEvent);

        Subscription subscriptionMediaViewPagerChangedEvent = RxBus.getDefault().toObservable(MediaViewPagerChangedEvent.class)
                .map(mediaViewPagerChangedEvent -> mediaViewPagerChangedEvent)
                .subscribe(new RxBusSubscriber<MediaViewPagerChangedEvent>() {
                    @Override
                    protected void onEvent(MediaViewPagerChangedEvent mediaPreviewViewPagerChangedEvent) {
                        int curIndex = mediaPreviewViewPagerChangedEvent.getCurIndex();
                        int totalSize = mediaPreviewViewPagerChangedEvent.getTotalSize();

                        String title;
                        if(mediaPreviewViewPagerChangedEvent.isPreview()){
                            title = getString(R.string.gallery_preview_page_title, curIndex + 1, totalSize);
                        } else {
                            if(mConfiguration.isImage()) {
                                title = getString(R.string.gallery_image_page_title, curIndex + 1, totalSize);
                            } else {
                                title = getString(R.string.gallery_video_page_title, curIndex + 1, totalSize);
                            }
                        }

                        mTvToolbarTitle.setText(title);
                    }
                });
        RxBus.getDefault().add(subscriptionMediaViewPagerChangedEvent);

        Subscription subscriptionOpenMediaPageFragmentEvent = RxBus.getDefault().toObservable(OpenMediaPageFragmentEvent.class)
                .map(openMediaPageFragmentEvent -> openMediaPageFragmentEvent)
                .subscribe(new RxBusSubscriber<OpenMediaPageFragmentEvent>() {
                    @Override
                    protected void onEvent(OpenMediaPageFragmentEvent openMediaPageFragmentEvent) throws Exception {
                        showMediaPageFragment();
                    }
                });
        RxBus.getDefault().add(subscriptionOpenMediaPageFragmentEvent);
    }

    public List<MediaBean> getCheckedList() {
        return mCheckedList;
    }

    private void backAction() {
        if(mMediaGridFragment.isVisible()) {
            onBackPressed();
        } else if(mMediaPreviewFragment.isVisible() || mMediaPageFragment.isVisible()){
            showMediaGridFragment();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            backAction();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().clear();
        RxBus.getDefault().removeAllStickyEvents();
    }

    private StateListDrawable createDefaultOverButtonBgDrawable() {
        int dp12 = (int) ThemeUtils.applyDimensionDp(this, 12.f);
        int dp8 = (int) ThemeUtils.applyDimensionDp(this, 8.f);
        float dp4 = ThemeUtils.applyDimensionDp(this, 4.f);
        float[] round = new float[] { dp4, dp4, dp4, dp4, dp4, dp4, dp4, dp4 };
        ShapeDrawable pressedDrawable = new ShapeDrawable(new RoundRectShape(round, null, null));
        pressedDrawable.setPadding(dp12, dp8, dp12, dp8);
        int pressedColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_over_button_pressed_color, R.color.gallery_default_toolbar_over_button_pressed_color);
        pressedDrawable.getPaint().setColor(pressedColor);

        int normalColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_over_button_normal_color, R.color.gallery_default_toolbar_over_button_normal_color);
        ShapeDrawable normalDrawable = new ShapeDrawable(new RoundRectShape(round, null, null));
        normalDrawable.setPadding(dp12, dp8, dp12, dp8);
        normalDrawable.getPaint().setColor(normalColor);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        return stateListDrawable;
    }
}
