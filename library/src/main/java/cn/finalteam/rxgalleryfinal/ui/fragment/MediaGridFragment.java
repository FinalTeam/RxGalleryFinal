package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.anim.Animation;
import cn.finalteam.rxgalleryfinal.anim.SlideInUnderneathAnimation;
import cn.finalteam.rxgalleryfinal.anim.SlideOutUnderneathAnimation;
import cn.finalteam.rxgalleryfinal.bean.BucketBean;
import cn.finalteam.rxgalleryfinal.bean.ImageCropBean;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.presenter.impl.MediaGridPresenterImpl;
import cn.finalteam.rxgalleryfinal.rxbus.RxBus;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.CloseMediaViewPageFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaCheckChangeEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPageFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPreviewFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.RequestStorageReadAccessPermissionEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.adapter.BucketAdapter;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaGridAdapter;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;
import cn.finalteam.rxgalleryfinal.ui.widget.FooterAdapter;
import cn.finalteam.rxgalleryfinal.ui.widget.HorizontalDividerItemDecoration;
import cn.finalteam.rxgalleryfinal.ui.widget.MarginDecoration;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerViewFinal;
import cn.finalteam.rxgalleryfinal.utils.CameraUtils;
import cn.finalteam.rxgalleryfinal.utils.DeviceUtils;
import cn.finalteam.rxgalleryfinal.utils.EmptyViewUtils;
import cn.finalteam.rxgalleryfinal.utils.FileUtils;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;
import cn.finalteam.rxgalleryfinal.utils.MediaUtils;
import cn.finalteam.rxgalleryfinal.utils.PermissionCheckUtils;
import cn.finalteam.rxgalleryfinal.utils.SimpleDateUtils;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;
import cn.finalteam.rxgalleryfinal.view.MediaGridView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/7 上午10:02
 * <p>
 * Desction: 直接暴漏
 * Author:KARL-Dujinyang
 * Date:2017.
 */
public class MediaGridFragment extends BaseFragment implements MediaGridView, RecyclerViewFinal.OnLoadMoreListener,
        FooterAdapter.OnItemClickListener, View.OnClickListener, MediaScanner.ScanCallback, BucketAdapter.OnRecyclerViewItemClickListener {

    private static final String IMAGE_TYPE = "image/jpeg";
    //接口-单选-是否裁剪
    public static IRadioImageCheckedListener iListenerRadio;
    //预留公开命名接口
    private static File mImageStoreDir;
    private static File mImageStoreCropDir; //裁剪目录
    //裁剪后+name
    private static File mCropPath = null;
    private final String IMAGE_STORE_FILE_NAME = "IMG_%s.jpg";
    private final String VIDEO_STORE_FILE_NAME = "IMG_%s.mp4";
    private final int TAKE_IMAGE_REQUEST_CODE = 1001;
    private final int CROP_IMAGE_REQUEST_CODE = 1011;
    private final String TAKE_URL_STORAGE_KEY = "take_url_storage_key";
    private final String BUCKET_ID_KEY = "bucket_id_key";
    private final int LIMIT = 23;
    MediaGridPresenterImpl mMediaGridPresenter;
    DisplayMetrics mScreenSize;
    private List<MediaBean> mMediaBeanList;
    private MediaGridAdapter mMediaGridAdapter;
    private RecyclerViewFinal mRvMedia;
    private LinearLayout mLlEmptyView;
    private RecyclerView mRvBucket;
    private BucketAdapter mBucketAdapter;
    private RelativeLayout mRlBucektOverview;
    private List<BucketBean> mBucketBeanList;
    private TextView mTvFolderName;
    private TextView mTvPreview;
    private RelativeLayout mRlRootView;
    //扫描
    private MediaScanner mMediaScanner;
    private int mPage = 1;
    private String mImagePath;

    private String mBucketId = String.valueOf(Integer.MIN_VALUE);

    private MediaActivity mMediaActivity;
    private Disposable mMediaCheckChangeDisposable;
    private Disposable mCloseMediaViewPageFragmentDisposable;
    private Disposable mRequestStorageReadAccessPermissionDisposable;

    private SlideInUnderneathAnimation slideInUnderneathAnimation;
    private SlideOutUnderneathAnimation slideOutUnderneathAnimation;

    private int uCropStatusColor;
    private int uCropToolbarColor;
    private int uCropActivityWidgetColor;
    private int uCropToolbarWidgetColor;
    private String uCropTitle;
    private String requestStorageAccessPermissionTips;

    public static MediaGridFragment newInstance(Configuration configuration) {
        MediaGridFragment fragment = new MediaGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIGURATION, configuration);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * getImageStoreDir
     *
     * @return 存储路径
     */
    public static File getImageStoreDirByFile() {
        return mImageStoreDir;
    }

    /**
     * getImageStoreDir
     *
     * @return 存储路径
     */
    public static String getImageStoreDirByStr() {
        if (mImageStoreDir != null)
            return mImageStoreDir.getPath();
        else
            return null;
    }

    /**
     * 设置路径
     */
    public static void setImageStoreDir(File imgFile) {
        Logger.i("设置图片保存路径为：" + imgFile.getAbsolutePath());
        mImageStoreDir = imgFile;
    }

    /**
     * 设置路径
     */
    public static void setImageStoreDir(String imgFile) {
        mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM" + File.separator + imgFile + File.separator);
        Logger.i("设置图片保存路径为：" + mImageStoreDir.getAbsolutePath());
    }

    /**
     * getImageStoreDir裁剪
     *
     * @return 裁剪存储路径
     */
    public static File getImageStoreCropDirByFile() {
        return mImageStoreCropDir;
    }

    /**
     * getImageStoreDir
     *
     * @return 存储路径
     */
    public static String getImageStoreCropDirByStr() {
        if (mImageStoreCropDir != null)
            return mImageStoreCropDir.getPath();
        else
            return null;
    }

    /**
     * 设置裁剪路径
     */
    public static void setImageStoreCropDir(File imgFile) {
        mImageStoreCropDir = imgFile;
        Logger.i("设置图片裁剪保存路径为：" + mImageStoreCropDir.getAbsolutePath());
    }

    /**
     * 设置裁剪路径
     *
     * @param imgFile 裁剪
     */
    public static void setImageStoreCropDir(String imgFile) {
        mImageStoreCropDir = new File(Environment.getExternalStorageDirectory(), "/DCIM" + File.separator + imgFile + File.separator);
        if (!mImageStoreCropDir.exists()) {
            mImageStoreCropDir.mkdirs();
        }
        Logger.i("设置图片裁剪保存路径为：" + mImageStoreCropDir.getAbsolutePath());
    }

    public static void setRadioListener(IRadioImageCheckedListener radioListener) {
        MediaGridFragment.iListenerRadio = radioListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MediaActivity) {
            mMediaActivity = (MediaActivity) context;
        }
        mMediaScanner = new MediaScanner(context);
    }

    @Override
    public int getContentView() {
        return R.layout.gallery_fragment_media_grid;
    }

    @Override
    public void onViewCreatedOk(View view, @Nullable Bundle savedInstanceState) {

        mRvMedia = (RecyclerViewFinal) view.findViewById(R.id.rv_media);
        mLlEmptyView = (LinearLayout) view.findViewById(R.id.ll_empty_view);
        mRvBucket = (RecyclerView) view.findViewById(R.id.rv_bucket);
        mRlBucektOverview = (RelativeLayout) view.findViewById(R.id.rl_bucket_overview);
        mRlRootView = (RelativeLayout) view.findViewById(R.id.rl_root_view);

        mRvMedia.setEmptyView(mLlEmptyView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRvMedia.addItemDecoration(new MarginDecoration(getContext()));
        mRvMedia.setLayoutManager(gridLayoutManager);
        mRvMedia.setOnLoadMoreListener(this);
        mRvMedia.setFooterViewHide(true);

        mTvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
        mTvFolderName.setOnClickListener(this);
        mTvPreview = (TextView) view.findViewById(R.id.tv_preview);
        mTvPreview.setOnClickListener(this);
        mTvPreview.setEnabled(false);
        if (mConfiguration.isRadio()) {
            view.findViewById(R.id.tv_preview_vr).setVisibility(View.GONE);
            mTvPreview.setVisibility(View.GONE);

        } else {
            if (mConfiguration.isHidePreview()) {
                view.findViewById(R.id.tv_preview_vr).setVisibility(View.GONE);
                mTvPreview.setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.tv_preview_vr).setVisibility(View.VISIBLE);
                mTvPreview.setVisibility(View.VISIBLE);
            }
        }

        mMediaBeanList = new ArrayList<>();
        mScreenSize = DeviceUtils.getScreenSize(getContext());
        mMediaGridAdapter = new MediaGridAdapter(mMediaActivity, mMediaBeanList, mScreenSize.widthPixels, mConfiguration);
        mRvMedia.setAdapter(mMediaGridAdapter);
        mMediaGridPresenter = new MediaGridPresenterImpl(getContext(), mConfiguration.isImage());
        mMediaGridPresenter.setMediaGridView(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRvBucket.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.gallery_bucket_list_decoration_color))
                .size(getResources().getDimensionPixelSize(R.dimen.gallery_divider_decoration_height))
                .margin(getResources().getDimensionPixelSize(R.dimen.gallery_bucket_margin),
                        getResources().getDimensionPixelSize(R.dimen.gallery_bucket_margin))
                .build());
        mRvBucket.setLayoutManager(linearLayoutManager);
        mBucketBeanList = new ArrayList<>();
        mBucketAdapter = new BucketAdapter(mBucketBeanList, mConfiguration, ContextCompat.getColor(getContext(), R.color.gallery_bucket_list_item_normal_color));
        mRvBucket.setAdapter(mBucketAdapter);
        mRvMedia.setOnItemClickListener(this);
        mMediaGridPresenter.getBucketList();
        mBucketAdapter.setOnRecyclerViewItemClickListener(this);

        mRlBucektOverview.setVisibility(View.INVISIBLE);

        if (slideInUnderneathAnimation == null) {
            slideInUnderneathAnimation = new SlideInUnderneathAnimation(mRvBucket);
        }

        slideInUnderneathAnimation
                .setDirection(Animation.DIRECTION_DOWN)
                .animate();

        subscribeEvent();

        Activity activity = mMediaActivity;
        if (activity == null) {
            activity = getActivity();
        }

        if (mConfiguration.isImage()) {
            mTvFolderName.setText(R.string.gallery_all_image);
        } else {
            mTvFolderName.setText(R.string.gallery_all_video);
        }

        String requestStorageAccessPermissionTips = ThemeUtils.resolveString(getContext(),
                R.attr.gallery_request_storage_access_permission_tips,
                R.string.gallery_default_request_storage_access_permission_tips);
        boolean success = PermissionCheckUtils.checkReadExternalPermission(activity, requestStorageAccessPermissionTips,
                MediaActivity.REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        if (success) {
            mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
        }


    }

    /**
     * RxBus
     */
    private void subscribeEvent() {
        mMediaCheckChangeDisposable = RxBus.getDefault().toObservable(MediaCheckChangeEvent.class)
                .subscribeWith(new RxBusDisposable<MediaCheckChangeEvent>() {
                    @Override
                    protected void onEvent(MediaCheckChangeEvent mediaCheckChangeEvent) {
                        if (mMediaActivity.getCheckedList().size() == 0) {
                            mTvPreview.setEnabled(false);
                        } else {
                            mTvPreview.setEnabled(true);
                        }

                    }
                });
        RxBus.getDefault().add(mMediaCheckChangeDisposable);

        mCloseMediaViewPageFragmentDisposable = RxBus.getDefault().toObservable(CloseMediaViewPageFragmentEvent.class)
                .subscribeWith(new RxBusDisposable<CloseMediaViewPageFragmentEvent>() {
                    @Override
                    protected void onEvent(CloseMediaViewPageFragmentEvent closeMediaViewPageFragmentEvent) throws Exception {
                        mMediaGridAdapter.notifyDataSetChanged();
                    }
                });
        RxBus.getDefault().add(mCloseMediaViewPageFragmentDisposable);

        mRequestStorageReadAccessPermissionDisposable = RxBus.getDefault().toObservable(RequestStorageReadAccessPermissionEvent.class)
                .subscribeWith(new RxBusDisposable<RequestStorageReadAccessPermissionEvent>() {
                    @Override
                    protected void onEvent(RequestStorageReadAccessPermissionEvent requestStorageReadAccessPermissionEvent) throws Exception {
                        if (requestStorageReadAccessPermissionEvent.getType() == RequestStorageReadAccessPermissionEvent.TYPE_WRITE) {
                            if (requestStorageReadAccessPermissionEvent.isSuccess()) {
                                mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
                            } else {
                                getActivity().finish();
                            }
                        } else {
                            if (requestStorageReadAccessPermissionEvent.isSuccess()) {
                                openCamera(mMediaActivity);
                            }
                        }
                    }
                });
        RxBus.getDefault().add(mRequestStorageReadAccessPermissionDisposable);

    }

    /**
     * 设置主题
     */
    @Override
    public void setTheme() {
        super.setTheme();
        uCropStatusColor = ThemeUtils.resolveColor(getActivity(), R.attr.gallery_ucrop_status_bar_color, R.color.gallery_default_ucrop_color_widget_active);
        uCropToolbarColor = ThemeUtils.resolveColor(getActivity(), R.attr.gallery_ucrop_toolbar_color, R.color.gallery_default_ucrop_color_widget_active);
        uCropActivityWidgetColor = ThemeUtils.resolveColor(getActivity(), R.attr.gallery_ucrop_activity_widget_color, R.color.gallery_default_ucrop_color_widget);
        uCropToolbarWidgetColor = ThemeUtils.resolveColor(getActivity(), R.attr.gallery_ucrop_toolbar_widget_color, R.color.gallery_default_toolbar_widget_color);
        uCropTitle = ThemeUtils.resolveString(getActivity(), R.attr.gallery_ucrop_toolbar_title, R.string.gallery_edit_phote);
        int pageColor = ThemeUtils.resolveColor(getContext(), R.attr.gallery_page_bg, R.color.gallery_default_page_bg);
        mRlRootView.setBackgroundColor(pageColor);
        requestStorageAccessPermissionTips = ThemeUtils.resolveString(getContext(), R.attr.gallery_request_camera_permission_tips, R.string.gallery_default_camera_access_permission_tips);
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onStart() {
        super.onStart();
        onLoadFile();
        //直接刷新一次
        //   refreshUI();
    }


    @Override
    public void loadMore() {
        mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
    }

    @Override
    public void onRequestMediaCallback(List<MediaBean> list) {
        if (!mConfiguration.isHideCamera()) {
            if (mPage == 1 && TextUtils.equals(mBucketId, String.valueOf(Integer.MIN_VALUE))) {
                MediaBean takePhotoBean = new MediaBean();
                takePhotoBean.setId(Integer.MIN_VALUE);
                takePhotoBean.setBucketId(String.valueOf(Integer.MIN_VALUE));
                mMediaBeanList.add(takePhotoBean);
            }
        }
        if (list != null && list.size() > 0) {
            mMediaBeanList.addAll(list);
            Logger.i(String.format("得到:%s张图片", list.size()));
        } else {
            Logger.i("没有更多图片");
        }
        mMediaGridAdapter.notifyDataSetChanged();

        mPage++;

        if (list == null || list.size() < LIMIT) {
            mRvMedia.setFooterViewHide(true);
            mRvMedia.setHasLoadMore(false);
        } else {
            mRvMedia.setFooterViewHide(false);
            mRvMedia.setHasLoadMore(true);
        }

        if (mMediaBeanList.size() == 0) {
            String mediaEmptyTils = ThemeUtils.resolveString(getContext(), R.attr.gallery_media_empty_tips, R.string.gallery_default_media_empty_tips);
            EmptyViewUtils.showMessage(mLlEmptyView, mediaEmptyTils);
        }

        mRvMedia.onLoadMoreComplete();
    }

    @Override
    public void onRequestBucketCallback(List<BucketBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        mBucketBeanList.addAll(list);
        mBucketAdapter.setSelectedBucket(list.get(0));
    }

    @Override
    public void onItemClick(View view, int position) {
        BucketBean bucketBean = mBucketBeanList.get(position);
        String bucketId = bucketBean.getBucketId();
        mRlBucektOverview.setVisibility(View.GONE);
        if (TextUtils.equals(mBucketId, bucketId)) {
            return;
        }
        mBucketId = bucketId;
        EmptyViewUtils.showLoading(mLlEmptyView);
        mRvMedia.setHasLoadMore(false);
        mMediaBeanList.clear();
        mMediaGridAdapter.notifyDataSetChanged();
        mTvFolderName.setText(bucketBean.getBucketName());
        mBucketAdapter.setSelectedBucket(bucketBean);
        mRvMedia.setFooterViewHide(true);
        mPage = 1;
        mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        onObItemClick(position);
    }

    public void onObItemClick(int position) {
        MediaBean mediaBean = mMediaBeanList.get(position);
        if (mediaBean.getId() == Integer.MIN_VALUE) {

            if (!CameraUtils.hasCamera(getContext())) {
                Toast.makeText(getContext(), R.string.gallery_device_no_camera_tips, Toast.LENGTH_SHORT).show();
                return;
            }

            boolean b = PermissionCheckUtils.checkCameraPermission(mMediaActivity, requestStorageAccessPermissionTips, MediaActivity.REQUEST_CAMERA_ACCESS_PERMISSION);
            if (b) {
                openCamera(mMediaActivity);
            }
        } else {
            if (mConfiguration.isRadio()) {
                if (mConfiguration.isImage()) {
                    radioNext(mediaBean);
                } else {
                    videoRadioNext(mediaBean);
                }
            } else {
                MediaBean firstBean = mMediaBeanList.get(0);
                ArrayList<MediaBean> gridMediaList = new ArrayList<>();
                gridMediaList.addAll(mMediaBeanList);
                int pos = position;
                if (firstBean.getId() == Integer.MIN_VALUE) {
                    pos = position - 1;
                    gridMediaList.clear();
                    List<MediaBean> list = mMediaBeanList.subList(1, mMediaBeanList.size());
                    gridMediaList.addAll(list);
                }
                RxBus.getDefault().post(new OpenMediaPageFragmentEvent(gridMediaList, pos));
            }
        }
    }

    /**
     * 处理 Video  选择  是否预览
     *
     * @param mediaBean
     */
    private void videoRadioNext(MediaBean mediaBean) {
        if (!mConfiguration.isVideoPreview()) {
            setPostMediaBean(mediaBean);
            getActivity().finish();
            return;
        }
        try {
            Intent openVideo = new Intent(Intent.ACTION_VIEW);
            openVideo.setDataAndType(Uri.parse(mediaBean.getOriginalPath()), "video/*");
            startActivity(openVideo);
        } catch (Exception e) {
            Toast.makeText(getContext(), "启动播放器失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理回调
     */
    private void setPostMediaBean(MediaBean mediaBean) {
        ImageCropBean bean = new ImageCropBean();
        bean.copyMediaBean(mediaBean);
        RxBus.getDefault().post(new ImageRadioResultEvent(bean));
    }

    /**
     * 区分功能
     */
    private void radioNext(MediaBean mediaBean) {
        Logger.i("isCrop :" + mConfiguration.isCrop());
        if (!mConfiguration.isCrop()) {
            setPostMediaBean(mediaBean);
            getActivity().finish();
        } else {
            //裁剪根据大家需求加上选择完图片后的回调
            setPostMediaBean(mediaBean);
            String originalPath = mediaBean.getOriginalPath();
            File file = new File(originalPath);
            Random random = new Random();
            String outName = String.format(IMAGE_STORE_FILE_NAME, SimpleDateUtils.getNowTime() + "_" + random.nextInt(1024));
            Logger.i("--->isCrop:" + mImageStoreCropDir);
            Logger.i("--->mediaBean.getOriginalPath():" + mediaBean.getOriginalPath());
            mCropPath = new File(mImageStoreCropDir, outName);
            Uri outUri = Uri.fromFile(mCropPath);
            if (!mImageStoreCropDir.exists()) {
                mImageStoreCropDir.mkdirs();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            Uri inputUri = Uri.fromFile(new File(mediaBean.getOriginalPath()));
            Intent intent = new Intent(getContext(), UCropActivity.class);


            // UCrop 参数 start
            Bundle bundle = new Bundle();

            bundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, outUri);
            bundle.putParcelable(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS, mediaBean);
            bundle.putInt(UCrop.Options.EXTRA_STATUS_BAR_COLOR, uCropStatusColor);
            bundle.putInt(UCrop.Options.EXTRA_TOOL_BAR_COLOR, uCropToolbarColor);
            bundle.putString(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR, uCropTitle);
            bundle.putInt(UCrop.Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE, uCropActivityWidgetColor);
            bundle.putInt(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, uCropToolbarWidgetColor);
            bundle.putBoolean(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS, mConfiguration.isHideBottomControls());
            bundle.putIntArray(UCrop.Options.EXTRA_ALLOWED_GESTURES, mConfiguration.getAllowedGestures());
            bundle.putInt(UCrop.Options.EXTRA_COMPRESSION_QUALITY, mConfiguration.getCompressionQuality());
            bundle.putInt(UCrop.Options.EXTRA_MAX_BITMAP_SIZE, mConfiguration.getMaxBitmapSize());
            bundle.putFloat(UCrop.Options.EXTRA_MAX_SCALE_MULTIPLIER, mConfiguration.getMaxScaleMultiplier());
            bundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_X, mConfiguration.getAspectRatioX());
            bundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_Y, mConfiguration.getAspectRatioY());
            bundle.putInt(UCrop.EXTRA_MAX_SIZE_X, mConfiguration.getMaxResultWidth());
            bundle.putInt(UCrop.EXTRA_MAX_SIZE_Y, mConfiguration.getMaxResultHeight());
            bundle.putInt(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, mConfiguration.getSelectedByDefault());
            bundle.putBoolean(UCrop.Options.EXTRA_FREE_STYLE_CROP, mConfiguration.isFreestyleCropEnabled());
            bundle.putParcelable(UCrop.EXTRA_INPUT_URI, inputUri);
            // UCrop 参数 end

            int bk = FileUtils.existImageDir(inputUri.getPath());
            Logger.i("--->" + inputUri.getPath());
            Logger.i("--->" + outUri.getPath());
            ArrayList<AspectRatio> aspectRatioList = new ArrayList<>();
            AspectRatio[] aspectRatios = mConfiguration.getAspectRatio();
            if (aspectRatios != null) {
                for (int i = 0; i < aspectRatios.length; i++) {
                    aspectRatioList.add(i, aspectRatios[i]);
                    Logger.i("自定义比例=>" + aspectRatioList.get(i).getAspectRatioX() + " " + aspectRatioList.get(i).getAspectRatioY());
                }
            }
            //  AspectRatio[]aspectRatios =  mConfiguration.getAspectRatio();
            bundle.putParcelableArrayList(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS, aspectRatioList);//EXTRA_CONFIGURATION
            intent.putExtras(bundle);
            if (bk != -1) {
                //裁剪
                startActivityForResult(intent, CROP_IMAGE_REQUEST_CODE);
            } else {
                Logger.w("点击图片无效");
            }
        }
    }

    public void openCamera(Context context) {


        boolean image = mConfiguration.isImage();

        Intent captureIntent = image ? new Intent(MediaStore.ACTION_IMAGE_CAPTURE) : new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) == null) {
            Toast.makeText(getContext(), R.string.gallery_device_camera_unable, Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String filename = String.format(image ? IMAGE_STORE_FILE_NAME : VIDEO_STORE_FILE_NAME, dateFormat.format(new Date()));
        Logger.i("openCamera：" + mImageStoreDir.getAbsolutePath());
        File fileImagePath = new File(mImageStoreDir, filename);
        mImagePath = fileImagePath.getAbsolutePath();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagePath));
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mImagePath);
            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // video : 1: 高质量  0 低质量
//        captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Logger.i(String.format("拍照成功,图片存储路径:%s", mImagePath));
            mMediaScanner.scanFile(mImagePath, mConfiguration.isImage() ? IMAGE_TYPE : "", this);
        } else if (requestCode == 222) {
            Toast.makeText(getActivity(), "摄像成功", Toast.LENGTH_SHORT).show();
        } else if (requestCode == CROP_IMAGE_REQUEST_CODE && data != null) {
            Logger.i("裁剪成功");
            refreshUI();
            onCropFinished();
        }
    }

    /**
     * 裁剪之后
     * setResult(RESULT_OK, new Intent()
     * .putExtra(UCrop.EXTRA_OUTPUT_URI, uri)
     * .putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio)
     * .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth)
     * .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight)
     */
    private void onCropFinished() {
        if (iListenerRadio != null && mCropPath != null) {
            if (mConfiguration.isCrop()) {
                iListenerRadio.cropAfter(mCropPath);
            }
        } else {
            Logger.i("# CropPath is null！# ");
        }
        //裁剪默认会关掉这个界面. 实现接口返回true则不关闭.
        if (iListenerRadio == null) {
            getActivity().finish();
        } else {
            boolean flag = iListenerRadio.isActivityFinish();
            Logger.i("# crop image is flag # :" + flag);
            if (flag)
                getActivity().finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mImagePath)) {
            outState.putString(TAKE_URL_STORAGE_KEY, mImagePath);
        }
        if (!TextUtils.isEmpty(mBucketId)) {
            outState.putString(BUCKET_ID_KEY, mBucketId);
        }
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {

    }


    //****************************************************************************

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        mImagePath = savedInstanceState.getString(TAKE_URL_STORAGE_KEY);
        mBucketId = savedInstanceState.getString(BUCKET_ID_KEY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaScanner.unScanFile();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_preview) {
            RxBus.getDefault().post(new OpenMediaPreviewFragmentEvent());
        } else if (id == R.id.tv_folder_name) {
            v.setEnabled(false);
            if (isShowRvBucketView()) {
                hideRvBucketView();
            } else {
                showRvBucketView();
            }
        }
    }

    public boolean isShowRvBucketView() {
        return mRlBucektOverview != null && mRlBucektOverview.getVisibility() == View.VISIBLE;
    }


    public void showRvBucketView() {
        if (mRlBucektOverview == null) {
            slideInUnderneathAnimation = new SlideInUnderneathAnimation(mRlBucektOverview);
        }
        mRlBucektOverview.setVisibility(View.VISIBLE);
        slideInUnderneathAnimation
                .setDirection(Animation.DIRECTION_DOWN)
                .setDuration(Animation.DURATION_DEFAULT)
                .setListener(animation -> mTvFolderName.setEnabled(true))
                .animate();
    }

    public void hideRvBucketView() {
        if (slideOutUnderneathAnimation == null) {
            slideOutUnderneathAnimation = new SlideOutUnderneathAnimation(mRvBucket);
        }
        slideOutUnderneathAnimation
                .setDirection(Animation.DIRECTION_DOWN)
                .setDuration(Animation.DURATION_DEFAULT)
                .setListener(animation -> {
                    mTvFolderName.setEnabled(true);
                    mRlBucektOverview.setVisibility(View.GONE);
                })
                .animate();
    }

    /**
     * Observable刷新图库
     */
    public void refreshUI() {
        try {
            Logger.i("->getImageStoreDirByFile().getPath().toString()：" + getImageStoreDirByFile().getPath());
            Logger.i("->getImageStoreCropDirByStr ().toString()：" + getImageStoreCropDirByStr());
            if (!TextUtils.isEmpty(mImagePath))
                mMediaScanner.scanFile(mImagePath, IMAGE_TYPE, this);
            if (mCropPath != null) {
                Logger.i("->mCropPath:" + mCropPath.getPath() + " " + IMAGE_TYPE);
                mMediaScanner.scanFile(mCropPath.getPath(), IMAGE_TYPE, this);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    @Override
    public void onScanCompleted(String[] images) {
        if (images == null || images.length == 0) {
            Logger.i("images empty");
            return;
        }

        // mediaBean 有可能为Null，onNext 做了处理，在 getMediaBeanWithImage 时候就不处理Null了
        Observable.create((ObservableOnSubscribe<MediaBean>) subscriber -> {
            MediaBean mediaBean =
                    mConfiguration.isImage() ? MediaUtils.getMediaBeanWithImage(getContext(), images[0])
                            :
                            MediaUtils.getMediaBeanWithVideo(getContext(), images[0]);
            subscriber.onNext(mediaBean);
            subscriber.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<MediaBean>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("获取MediaBean异常" + e.toString());
                    }

                    @Override
                    public void onNext(MediaBean mediaBean) {
                        if (!isDetached() && mediaBean != null) {
                            int bk = FileUtils.existImageDir(mediaBean.getOriginalPath());
                            if (bk != -1) {
                                mMediaBeanList.add(1, mediaBean);
                                mMediaGridAdapter.notifyDataSetChanged();
                            } else {
                                Logger.i("获取：无");
                            }
                        }

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().remove(mMediaCheckChangeDisposable);
        RxBus.getDefault().remove(mCloseMediaViewPageFragmentDisposable);

    }

    /**
     * onAttach 转 onStart
     */
    public void onLoadFile() {
        //没有的话就默认路径
        if (getImageStoreDirByFile() == null && getImageStoreDirByStr() == null) {
            mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/IMMQY/");
            setImageStoreCropDir(mImageStoreDir);
        }
        if (!mImageStoreDir.exists()) {
            mImageStoreDir.mkdirs();
        }
        if (getImageStoreCropDirByFile() == null && getImageStoreCropDirByStr() == null) {
            mImageStoreCropDir = new File(mImageStoreDir, "crop");
            if (!mImageStoreCropDir.exists()) {
                mImageStoreCropDir.mkdirs();
            }
            setImageStoreCropDir(mImageStoreCropDir);
        }
    }
}
