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

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.event.CloseMediaViewPageFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaCheckChangeEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaViewPagerChangedEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaPreviewAdapter;
import cn.finalteam.rxgalleryfinal.utils.DeviceUtils;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;

/**
 * Desction:图片预览
 * Author:pengjianbo
 * Date:16/6/9 上午1:35
 */
public class MediaPreviewFragment extends BaseFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener{

    private static final String EXTRA_PAGE_INDEX = EXTRA_PREFIX + ".PageIndex";

    DisplayMetrics mScreenSize;

    private AppCompatCheckBox mCbCheck;
    private ViewPager mViewPager;
    private MediaPreviewAdapter mMediaPreviewAdapter;
    private List<MediaBean> mMediaBeanList;
    private RelativeLayout mRlRootView;

    private MediaActivity mMediaActivity;
    private int mPagerPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  MediaActivity) {
            mMediaActivity = (MediaActivity) context;
        }
    }

    public static MediaPreviewFragment newInstance(Configuration configuration, int position){
        MediaPreviewFragment fragment = new MediaPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIGURATION, configuration);
        bundle.putInt(EXTRA_PAGE_INDEX, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.gallery_fragment_media_preview;
    }


    @Override
    public void onViewCreatedOk(View view, @Nullable Bundle savedInstanceState) {
        mCbCheck = (AppCompatCheckBox) view.findViewById(R.id.cb_check);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mRlRootView = (RelativeLayout) view.findViewById(R.id.rl_root_view);
        mScreenSize = DeviceUtils.getScreenSize(getContext());
        mMediaBeanList = new ArrayList<>();
        if(mMediaActivity.getCheckedList() != null){
            mMediaBeanList.addAll(mMediaActivity.getCheckedList());
        }
        mMediaPreviewAdapter = new MediaPreviewAdapter(getContext(), mMediaBeanList,
                mScreenSize.widthPixels, mScreenSize.heightPixels, mConfiguration);
        mViewPager.setAdapter(mMediaPreviewAdapter);
        mCbCheck.setOnClickListener(this);

        if(savedInstanceState != null) {
            mPagerPosition = savedInstanceState.getInt(EXTRA_PAGE_INDEX);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewPager.setCurrentItem(mPagerPosition, false);
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
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            mPagerPosition = savedInstanceState.getInt(EXTRA_PAGE_INDEX);
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {
        if(outState != null){
            outState.putInt(EXTRA_PAGE_INDEX, mPagerPosition);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPagerPosition = position;
        MediaBean mediaBean = mMediaBeanList.get(position);
        mCbCheck.setChecked(false);
        //判断是否选择
        if(mMediaActivity != null && mMediaActivity.getCheckedList() != null){
            mCbCheck.setChecked(mMediaActivity.getCheckedList().contains(mediaBean));
        }

        RxBus.getDefault().post(new MediaViewPagerChangedEvent(position, mMediaBeanList.size(), true));
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
        mPagerPosition = 0;
        RxBus.getDefault().post(new CloseMediaViewPageFragmentEvent());
    }
}
