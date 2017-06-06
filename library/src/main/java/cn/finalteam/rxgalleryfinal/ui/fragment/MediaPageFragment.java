package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPageFragmentEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaPreviewAdapter;
import cn.finalteam.rxgalleryfinal.utils.DeviceUtils;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 下午10:02
 */
public class MediaPageFragment extends BaseFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener {

    private static final String EXTRA_MEDIA_LIST = EXTRA_PREFIX + ".MediaList";
    private static final String EXTRA_ITEM_CLICK_POSITION = EXTRA_PREFIX + ".ItemClickPosition";

    DisplayMetrics mScreenSize;

    private AppCompatCheckBox mCbCheck;
    private ViewPager mViewPager;
    private MediaPreviewAdapter mMediaPreviewAdapter;
    private ArrayList<MediaBean> mMediaBeanList;
    private RelativeLayout mRlRootView;

    private MediaActivity mMediaActivity;
    private int mItemClickPosition;

    public static MediaPageFragment newInstance(Configuration configuration, ArrayList<MediaBean> list, int position) {
        MediaPageFragment fragment = new MediaPageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIGURATION, configuration);
        bundle.putParcelableArrayList(EXTRA_MEDIA_LIST, list);
        bundle.putInt(EXTRA_ITEM_CLICK_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MediaActivity) {
            mMediaActivity = (MediaActivity) context;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.gallery_fragment_media_page;
    }

    @Override
    public void onViewCreatedOk(View view, @Nullable Bundle savedInstanceState) {
        mCbCheck = (AppCompatCheckBox) view.findViewById(R.id.cb_page_check);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_page);
        mRlRootView = (RelativeLayout) view.findViewById(R.id.rl_page_root_view);
        mScreenSize = DeviceUtils.getScreenSize(getContext());

        mMediaBeanList = new ArrayList<>();
        if (savedInstanceState != null) {
            List<MediaBean> mediaList = savedInstanceState.getParcelableArrayList(EXTRA_MEDIA_LIST);
            mItemClickPosition = savedInstanceState.getInt(EXTRA_ITEM_CLICK_POSITION);

            if (mediaList != null) {
                mMediaBeanList.addAll(mediaList);
            }
        }
        mMediaPreviewAdapter = new MediaPreviewAdapter(mMediaBeanList,
                mScreenSize.widthPixels, mScreenSize.heightPixels, mConfiguration
                , ThemeUtils.resolveColor(getActivity(), R.attr.gallery_page_bg, R.color.gallery_default_page_bg),
                ContextCompat.getDrawable(getActivity(), ThemeUtils.resolveDrawableRes(getActivity(), R.attr.gallery_default_image, R.drawable.gallery_default_image)));
        mViewPager.setAdapter(mMediaPreviewAdapter);
        mCbCheck.setOnClickListener(this);
        mViewPager.setCurrentItem(mItemClickPosition);
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mConfiguration == null || mMediaBeanList.size() == 0
                || mCbCheck == null || mViewPager == null) {
            return;
        }
        MediaBean mediaBean = mMediaBeanList.get(mItemClickPosition);
        if (mMediaActivity != null && mMediaActivity.getCheckedList() != null) {
            if (mMediaActivity.getCheckedList().contains(mediaBean)) {
                mCbCheck.setChecked(true);
            }
        }
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
        if (savedInstanceState == null) {
            return;
        }
        List<MediaBean> mediaList = savedInstanceState.getParcelableArrayList(EXTRA_MEDIA_LIST);
        mItemClickPosition = savedInstanceState.getInt(EXTRA_ITEM_CLICK_POSITION);
        if (mediaList != null) {
            mMediaBeanList.clear();
            Logger.i("恢复数据:" + mediaList.size() + "  d=" + mediaList.get(0).getOriginalPath());
            mMediaBeanList.addAll(mediaList);
        }
        mViewPager.setCurrentItem(mItemClickPosition);
        mMediaPreviewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveState(Bundle outState) {
        if (outState == null) {
            return;
        }
        outState.putParcelableArrayList(EXTRA_MEDIA_LIST, mMediaBeanList);
        outState.putInt(EXTRA_ITEM_CLICK_POSITION, mItemClickPosition);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mItemClickPosition = position;

        MediaBean mediaBean = mMediaBeanList.get(position);
        //判断是否选择
        if (mMediaActivity != null && mMediaActivity.getCheckedList() != null) {
            mCbCheck.setChecked(mMediaActivity.getCheckedList().contains(mediaBean));
        } else {
            mCbCheck.setChecked(false);
        }

        RxBus.getDefault().post(new MediaViewPagerChangedEvent(position, mMediaBeanList.size(), false));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 改变选择
     */
    @Override
    public void onClick(View view) {
        if (mMediaBeanList.size() == 0) {
            return;
        }

        int position = mViewPager.getCurrentItem();
        MediaBean mediaBean = mMediaBeanList.get(position);
        if (mConfiguration.getMaxSize() == mMediaActivity.getCheckedList().size()
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
        RxBus.getDefault().removeStickyEvent(OpenMediaPageFragmentEvent.class);
        RxBus.getDefault().post(new CloseMediaViewPageFragmentEvent());
    }
}
