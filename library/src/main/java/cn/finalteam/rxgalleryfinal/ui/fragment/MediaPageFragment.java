package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.component.BaseComponent;
import cn.finalteam.rxgalleryfinal.di.component.DaggerBaseComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.BaseModule;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.CloseMediaViewPageFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaCheckChangeEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaViewPagerChangedEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.SendMediaPageFragmentDataEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaPreviewAdapter;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;
import rx.Subscription;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 下午10:02
 */
public class MediaPageFragment extends BaseFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener{

    @Inject
    Configuration mConfiguration;
    @Inject
    DisplayMetrics mScreenSize;

    private AppCompatCheckBox mCbCheck;
    private ViewPager mViewPager;
    private MediaPreviewAdapter mMediaPreviewAdapter;
    private List<MediaBean> mMediaBeanList;
    private RelativeLayout mRlRootView;

    private MediaActivity mMediaActivity;
    private int mItemClickPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  MediaActivity) {
            mMediaActivity = (MediaActivity) context;
        }
    }

    public static MediaPageFragment newInstance(){
        return new MediaPageFragment();
    }

    @Override
    public int getContentView() {
        return R.layout.gallery_fragment_media_page;
    }

    @Override
    protected void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent) {
        BaseComponent baseComponent = DaggerBaseComponent.builder()
                .rxGalleryFinalComponent(rxGalleryFinalComponent)
                .baseModule(new BaseModule())
                .build();
        baseComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCbCheck = (AppCompatCheckBox) view.findViewById(R.id.cb_page_check);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_page);
        mRlRootView = (RelativeLayout) view.findViewById(R.id.rl_page_root_view);

        mMediaBeanList = new ArrayList<>();
        mMediaPreviewAdapter = new MediaPreviewAdapter(getContext(), mMediaBeanList,
                mScreenSize.widthPixels, mScreenSize.heightPixels, mConfiguration);
        mViewPager.setAdapter(mMediaPreviewAdapter);
        mCbCheck.setOnClickListener(this);
        subscribeEvent();
    }

    private void subscribeEvent() {
        Subscription subscriptionSendMediaPageFragmentDataEvent = RxBus.getDefault().toObservableSticky(SendMediaPageFragmentDataEvent.class)
                .subscribe(new RxBusSubscriber<SendMediaPageFragmentDataEvent>() {
                    @Override
                    protected void onEvent(SendMediaPageFragmentDataEvent sendMediaPageFragmentDataEvent) {
                        mMediaBeanList.clear();
                        mItemClickPosition = sendMediaPageFragmentDataEvent.getPosition();
                        mMediaBeanList.addAll(sendMediaPageFragmentDataEvent.getMediaBeanList());
                        mMediaPreviewAdapter.notifyDataSetChanged();

                        RxBus.getDefault().post(new MediaViewPagerChangedEvent(mItemClickPosition, mMediaBeanList.size(), false));
                    }
                });
        RxBus.getDefault().add(subscriptionSendMediaPageFragmentDataEvent);
    }


    @Override
    public void onStart() {
        super.onStart();
        mViewPager.setCurrentItem(mItemClickPosition, false);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void setTheme() {
        super.setTheme();
        int checkTint = ThemeUtils.resolveColor(getContext(), R.attr.gallery_checkbox_button_tint_color, R.color.gallery_default_checkbox_button_tint_color);
        CompoundButtonCompat.setButtonTintList(mCbCheck, ColorStateList.valueOf(checkTint));
        int cbTextColor = ThemeUtils.resolveColor(getContext(), R.attr.gallery_checkbox_text_color, R.color.gallery_default_checkbox_text_color);
        mCbCheck.setTextColor(cbTextColor);

        int pageColor = ThemeUtils.resolveColor(getContext(), R.attr.gallery_page_bg, R.color.gallery_default_page_bg);
        mRlRootView.setBackgroundColor(pageColor);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mItemClickPosition = position;
        MediaBean mediaBean = mMediaBeanList.get(position);
        mCbCheck.setChecked(false);
        //判断是否选择
        if(mMediaActivity != null && mMediaActivity.getCheckedList() != null){
            mCbCheck.setChecked(mMediaActivity.getCheckedList().contains(mediaBean));
        }

        RxBus.getDefault().post(new MediaViewPagerChangedEvent(position, mMediaBeanList.size(), false));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 改变选择
     * @param view
     */
    @Override
    public void onClick(View view) {
        int position = mViewPager.getCurrentItem();
        MediaBean mediaBean = mMediaBeanList.get(position);
        if(mConfiguration.getMaxSize() == mMediaActivity.getCheckedList().size()
                && !mMediaActivity.getCheckedList().contains(mediaBean)) {
            Toast.makeText(getContext(), getResources()
                    .getString(R.string.gallery_image_max_size_tip, mConfiguration.getMaxSize()), Toast.LENGTH_SHORT).show();
            mCbCheck.setChecked(false);
        } else {
            RxBus.getDefault().post(new MediaCheckChangeEvent(mediaBean));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mItemClickPosition = 0;
        RxBus.getDefault().removeStickyEvent(SendMediaPageFragmentDataEvent.class);
        RxBus.getDefault().post(new CloseMediaViewPageFragmentEvent());
    }
}
