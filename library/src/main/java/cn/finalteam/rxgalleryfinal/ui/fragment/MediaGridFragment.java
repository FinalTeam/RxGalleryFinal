package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import cn.finalteam.rxgalleryfinal.rxbus.RxBusSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.CloseMediaViewPageFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.MediaCheckChangeEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPreviewFragmentEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.RequestStorageReadAccessPermissionEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPageFragmentEvent;
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
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:02
 *
 * Desction: 直接暴漏
 * Author:KARL-Dujinyang
 * Date:2017.
 *
 */
public class MediaGridFragment extends BaseFragment implements MediaGridView, RecyclerViewFinal.OnLoadMoreListener,
        FooterAdapter.OnItemClickListener,View.OnClickListener, MediaScanner.ScanCallback, BucketAdapter.OnRecyclerViewItemClickListener {

    private final String IMAGE_STORE_FILE_NAME = "IMG_%s.jpg";
    private static String imgType = "image/jpeg" ;
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
    //接口-单选-是否裁剪
    public static IRadioImageCheckedListener iListenerRadio;

    private int mPage = 1;
    //预留公开命名接口
    private static File mImageStoreDir;
    private static File mImageStoreCropDir; //裁剪目录
    //裁剪后+name
    private static File mCropPath  = null;

    private String mImagePath;

    private String mBucketId = String.valueOf(Integer.MIN_VALUE);

    private MediaActivity mMediaActivity;
    private Subscription mSubscrMediaCheckChangeEvent;
    private Subscription mSubscrCloseMediaViewPageFragmentEvent;
    private Subscription mSubscrRequestStorageReadAccessPermissionEvent;

    public static MediaGridFragment newInstance(Configuration configuration) {
        MediaGridFragment fragment = new MediaGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIGURATION, configuration);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MediaActivity) {
            mMediaActivity = (MediaActivity) context;
        }
        //onLoadFile()
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
        if(mConfiguration.isRadio()){
            view.findViewById(R.id.tv_preview_vr).setVisibility(View.GONE);
            mTvPreview.setVisibility(View.GONE);
        }

        mMediaBeanList = new ArrayList<>();
        mScreenSize = DeviceUtils.getScreenSize(getContext());
        mMediaGridAdapter = new MediaGridAdapter(mMediaActivity, mMediaBeanList,
                mScreenSize.widthPixels, mConfiguration);
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
        mBucketAdapter = new BucketAdapter(getContext(), mBucketBeanList, mConfiguration);
        mRvBucket.setAdapter(mBucketAdapter);
        mRvMedia.setOnItemClickListener(this);
        mMediaGridPresenter.getBucketList();
        mBucketAdapter.setOnRecyclerViewItemClickListener(this);

        mRlBucektOverview.setVisibility(View.INVISIBLE);
        new SlideInUnderneathAnimation(mRvBucket)
                .setDirection(Animation.DIRECTION_DOWN)
                .animate();

        subscribeEvent();

        Activity activity = mMediaActivity;
        if(activity == null){
            activity = getActivity();
        }

        String requestStorageAccessPermissionTips = ThemeUtils.resolveString(getContext(),
                R.attr.gallery_request_storage_access_permission_tips,
                R.string.gallery_default_request_storage_access_permission_tips);
        boolean success = PermissionCheckUtils.checkReadExternalPermission(activity, requestStorageAccessPermissionTips,
                MediaActivity.REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        if(success) {
            mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
        }

    }

    /**
     * RxBus
     */
    private void subscribeEvent() {
        mSubscrMediaCheckChangeEvent = RxBus.getDefault().toObservable(MediaCheckChangeEvent.class)
                .subscribe(new RxBusSubscriber<MediaCheckChangeEvent>() {
                    @Override
                    protected void onEvent(MediaCheckChangeEvent mediaCheckChangeEvent) {
                        if(mMediaActivity.getCheckedList().size() == 0){
                            mTvPreview.setEnabled(false);
                        } else {
                            mTvPreview.setEnabled(true);
                        }

                    }
                });
        RxBus.getDefault().add(mSubscrMediaCheckChangeEvent);

        mSubscrCloseMediaViewPageFragmentEvent = RxBus.getDefault().toObservable(CloseMediaViewPageFragmentEvent.class)
                .subscribe(new RxBusSubscriber<CloseMediaViewPageFragmentEvent>() {
                    @Override
                    protected void onEvent(CloseMediaViewPageFragmentEvent closeMediaViewPageFragmentEvent) throws Exception {
                        mMediaGridAdapter.notifyDataSetChanged();
                    }
                });
        RxBus.getDefault().add(mSubscrCloseMediaViewPageFragmentEvent);

        mSubscrRequestStorageReadAccessPermissionEvent = RxBus.getDefault().toObservable(RequestStorageReadAccessPermissionEvent.class)
                .subscribe(new RxBusSubscriber<RequestStorageReadAccessPermissionEvent>() {
                    @Override
                    protected void onEvent(RequestStorageReadAccessPermissionEvent requestStorageReadAccessPermissionEvent) throws Exception {
                        if(requestStorageReadAccessPermissionEvent.isSuccess()){
                            mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
                        } else {
                            getActivity().finish();
                        }
                    }
                });
        RxBus.getDefault().add(mSubscrRequestStorageReadAccessPermissionEvent);

    }

    /**
     * 设置主题
     */
    @Override
    public void setTheme() {
        super.setTheme();
        int pageColor = ThemeUtils.resolveColor(getContext(), R.attr.gallery_page_bg, R.color.gallery_default_page_bg);
        mRlRootView.setBackgroundColor(pageColor);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void loadMore() {
        mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
    }

    @Override
    public void onRequestMediaCallback(List<MediaBean> list) {
        if(!mConfiguration.isHideCamera()) {
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
        if(list == null || list.size() == 0){
            return;
        }

        mBucketBeanList.clear();
        mBucketBeanList.addAll(list);
        mBucketAdapter.setSelectedBucket(list.get(0));
        mBucketAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        BucketBean bucketBean = mBucketBeanList.get(position);
        String bucketId = bucketBean.getBucketId();
        mRlBucektOverview.setVisibility(View.GONE);
        if(TextUtils.equals(mBucketId, bucketId)){
            return;
        }
        mBucketId = bucketId;
        EmptyViewUtils.showLoading(mLlEmptyView);
        mRvMedia.setHasLoadMore(false);
        mMediaBeanList.clear();
        mMediaGridAdapter.notifyDataSetChanged();
        mBucketAdapter.setSelectedBucket(bucketBean);

        mRvMedia.setFooterViewHide(true);
        mPage = 1;
        mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        onObItemClick(position);
    }

    public void onObItemClick( int position) {
        MediaBean mediaBean = mMediaBeanList.get(position);
        if (mediaBean.getId() == Integer.MIN_VALUE) {

            if (!CameraUtils.hasCamera(getContext())) {
                Toast.makeText(getContext(), R.string.gallery_device_no_camera_tips, Toast.LENGTH_SHORT).show();
                return;
            }
            //打开
            openCamera(getActivity());

        } else {
            if (mConfiguration.isRadio()) {
                radioNext(mediaBean);
            } else {
                MediaBean firstBean = mMediaBeanList.get(0);
                ArrayList<MediaBean> gridMediaList = new ArrayList<>();
                gridMediaList.addAll(mMediaBeanList);
                int pos = position;
                if(firstBean.getId() == Integer.MIN_VALUE) {
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
     * 处理回调
     * @param mediaBean
     */
    private void setPostMediaBean(MediaBean mediaBean){
        ImageCropBean bean = new ImageCropBean();
        bean.copyMediaBean(mediaBean);
        RxBus.getDefault().post(new ImageRadioResultEvent(bean));
    }

    /**
     * 区分功能
     * @param mediaBean
     */
    private void radioNext(MediaBean mediaBean) {
        Logger.i("isCrop :" + mConfiguration.isCrop());
        if(!mConfiguration.isCrop()){
            setPostMediaBean(mediaBean);
            getActivity().finish();
        } else {
            //裁剪根据大家需求加上选择完图片后的回调
            setPostMediaBean(mediaBean);
            String originalPath = mediaBean.getOriginalPath();
            File file = new File(originalPath);
            Random random = new Random();
            String outName =String.format(IMAGE_STORE_FILE_NAME,SimpleDateUtils.getNowTime()+"_"+random.nextInt(1024));
            Logger.i("--->isCrop:"+mImageStoreCropDir);
            Logger.i("--->mediaBean.getOriginalPath():"+mediaBean.getOriginalPath());
            mCropPath =  new File(mImageStoreCropDir,outName );
            Uri outUri = Uri.fromFile(mCropPath);
            if(!mImageStoreCropDir.exists()){
                mImageStoreCropDir.mkdirs();
            }
            if(!file.exists()){
                file.mkdirs();
            }
            Uri inputUri = Uri.fromFile(new File(mediaBean.getOriginalPath()));
            Intent intent = new Intent(getContext(), UCropActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, outUri);
            bundle.putParcelable(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS, mediaBean);//EXTRA_INPUT_BEAN
            bundle.putParcelable(UCrop.EXTRA_INPUT_URI, inputUri);
            int bk = FileUtils.existImageDir(inputUri.getPath());
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Logger.i("--->" + inputUri.getPath());
            Logger.i("--->" + outUri.getPath());
            ArrayList<AspectRatio> aspectRatioList = new ArrayList<>();
            AspectRatio[]aspectRatios =  mConfiguration.getAspectRatio();
            if(aspectRatios != null) {
                for (int i = 0; i < aspectRatios.length; i++) {
                    aspectRatioList.add(i, aspectRatios[i]);
                    Logger.i("自定义比例=>" + aspectRatioList.get(i).getAspectRatioX() + " " + aspectRatioList.get(i).getAspectRatioY());
                }
            }
            //  AspectRatio[]aspectRatios =  mConfiguration.getAspectRatio();
            bundle.putParcelableArrayList(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS, aspectRatioList);//EXTRA_CONFIGURATION
            intent.putExtras(bundle);
            if(bk != -1){
                //裁剪
                startActivityForResult(intent,CROP_IMAGE_REQUEST_CODE);
            }else{
                Logger.w("点击图片无效");
            }
        }
    }





    /**
     * @Author:Karl-dujinyang
     * 兼容7.0打开路径问题
     */
    public void openCamera(Context context) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String filename = String.format(IMAGE_STORE_FILE_NAME, dateFormat.format(new Date()));
            Logger.i("openCamera：" + mImageStoreDir.getAbsolutePath());
            File fileImagePath = new File(mImageStoreDir, filename);
            mImagePath = fileImagePath.getAbsolutePath();
            if(mImagePath!=null){
                    /*获取当前系统的android版本号*/
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion < 24){
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagePath));
                    startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
                }else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, mImagePath );
                    Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
                }
            }
        } else {
            Toast.makeText(getContext(), R.string.gallery_device_camera_unable, Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Logger.i(String.format("拍照成功,图片存储路径:%s", mImagePath));
            //刷新相册数据库
            mMediaScanner.scanFile(mImagePath, imgType, this);
        }else if(requestCode == CROP_IMAGE_REQUEST_CODE){
            Logger.i("裁剪成功");
            refreshUI();
            onCropFinished(data);
        }
    }

    /**
     * 裁剪之后
     *  setResult(RESULT_OK, new Intent()
     .putExtra(UCrop.EXTRA_OUTPUT_URI, uri)
     .putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio)
     .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth)
     .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight)
     */
    private void onCropFinished(Intent data) {
        if(iListenerRadio != null) {
            if(mConfiguration.isCrop()) {
                Uri outputUri = data.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);
                Logger.i("# crop image is #" + outputUri.getPath());
                iListenerRadio.cropAfter(mCropPath);
            }
        }else{
            Logger.i("# CropPath is null！# ");
        }
        //裁剪默认会关掉这个界面. 实现接口返回true则不关闭.
        if(iListenerRadio==null) {
            getActivity().finish();
        } else {
            boolean flag = iListenerRadio.isActivityFinish();
            Logger.i("# crop image is flag # :"+flag);
            if(flag==true)
                getActivity().finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mImagePath)) {
            outState.putString(TAKE_URL_STORAGE_KEY, mImagePath);
        }
        if(!TextUtils.isEmpty(mBucketId)) {
            outState.putString(BUCKET_ID_KEY, mBucketId);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onRestoreState(Bundle savedInstanceState) {

    }

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
        if(id == R.id.tv_preview) {
            RxBus.getDefault().post(new OpenMediaPreviewFragmentEvent());
        } else if(id == R.id.tv_folder_name) {
            v.setEnabled(false);
            int visibility = mRlBucektOverview.getVisibility();
            if(visibility == View.VISIBLE) {
                new SlideOutUnderneathAnimation(mRvBucket)
                        .setDirection(Animation.DIRECTION_DOWN)
                        .setDuration(Animation.DURATION_DEFAULT)
                        .setListener(animation -> {
                            v.setEnabled(true);
                            mRlBucektOverview.setVisibility(View.GONE);
                        })
                        .animate();
            } else  {
                mRlBucektOverview.setVisibility(View.VISIBLE);
                new SlideInUnderneathAnimation(mRvBucket)
                        .setDirection(Animation.DIRECTION_DOWN)
                        .setDuration(Animation.DURATION_DEFAULT)
                        .setListener(animation -> {
                            v.setEnabled(true);
                        })
                        .animate();
            }
        }
    }

    /**
     * Observable刷新图库
     */
    public void refreshUI(){
        try {
            Logger.i("->getImageStoreDirByFile().getPath().toString()："+getImageStoreDirByFile().getPath().toString());
            Logger.i("->getImageStoreCropDirByStr ().toString()：" + getImageStoreCropDirByStr().toString());
    /*        if(getImageStoreDirByFile() !=null ){
                mMediaScanner.scanFile(getImageStoreDirByFile().getPath().toString(), "image/jpeg", this);
            }*/
            //刷新相册数据库
            if(!TextUtils.isEmpty(mImagePath))
                mMediaScanner.scanFile(mImagePath, imgType , this);
            if(mCropPath !=null) {
                Logger.i("->mCropPath:" + mCropPath.getPath() +" "+imgType);
                mMediaScanner.scanFile(mCropPath.getPath(), imgType, this);
            }
        }catch (Exception e ){
            Logger.e(e.getMessage());
        }
    }

    @Override
    public void onScanCompleted(String[] images) {
        if(images == null || images.length == 0){
            Logger.i("images empty");
            return;
        }

        mMediaGridPresenter.getBucketList();

        Observable.create((Observable.OnSubscribe<MediaBean>) subscriber -> {
            MediaBean mediaBean = MediaUtils.getMediaBeanWithImage(getContext(), images[0]);
            subscriber.onNext(mediaBean);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MediaBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("获取MediaBean异常");
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
        RxBus.getDefault().remove(mSubscrMediaCheckChangeEvent);
        RxBus.getDefault().remove(mSubscrCloseMediaViewPageFragmentEvent);

    }


    //****************************************************************************

    /**
     * getImageStoreDir
     * @return 存储路径
     */
    public static File getImageStoreDirByFile(){
        return mImageStoreDir;
    }

    /**
     * getImageStoreDir
     * @return 存储路径
     */
    public static String getImageStoreDirByStr(){
        if(mImageStoreDir!=null)
            return mImageStoreDir.getPath().toString();
        else
            return null;
    }

    /**
     * 设置路径
     * @param imgFile
     */
    public static void setImageStoreDir(File imgFile){
        Logger.i("设置图片保存路径为："+imgFile.getAbsolutePath());
        mImageStoreDir = imgFile;
    }

    /**
     * 设置路径
     * @param imgFile
     */
    public static void setImageStoreDir(String imgFile){
        mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM"+File.separator+imgFile+File.separator);;
        Logger.i("设置图片保存路径为："+mImageStoreDir.getAbsolutePath());
    }



    /**
     * getImageStoreDir裁剪
     * @return 裁剪存储路径
     */
    public static File getImageStoreCropDirByFile(){
        return mImageStoreCropDir;
    }

    /**
     * getImageStoreDir
     * @return 存储路径
     */
    public static String getImageStoreCropDirByStr(){
        if(mImageStoreCropDir!=null)
            return mImageStoreCropDir.getPath().toString();
        else
            return null;
    }


    /**
     * 设置裁剪路径
     * @param imgFile
     */
    public static void setImageStoreCropDir(File imgFile){
        mImageStoreCropDir = imgFile;
        Logger.i("设置图片裁剪保存路径为："+mImageStoreCropDir.getAbsolutePath());
    }

    /**
     * 设置裁剪路径
     * @param imgFile 裁剪
     */
    public static void setImageStoreCropDir(String imgFile){
        mImageStoreCropDir = new File(Environment.getExternalStorageDirectory(), "/DCIM"+File.separator+imgFile+File.separator);
        if(!mImageStoreCropDir.exists()){
            mImageStoreCropDir.mkdirs();
        }
        Logger.i("设置图片裁剪保存路径为："+mImageStoreCropDir.getAbsolutePath());
    }

    /**
     * onAttach 转 onStart
     */
    public void onLoadFile(){
        //没有的话就默认路径
        if(getImageStoreDirByFile()==null && getImageStoreDirByStr() == null){
            mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/IMMQY/");
            setImageStoreCropDir(mImageStoreDir);
        }
        if(!mImageStoreDir.exists()){
            mImageStoreDir.mkdirs();
        }
        if(getImageStoreCropDirByFile()== null && getImageStoreCropDirByStr() == null){
            mImageStoreCropDir = new File(mImageStoreDir, "crop");
            if (!mImageStoreCropDir.exists()) {
                mImageStoreCropDir.mkdirs();
            }
            setImageStoreCropDir(mImageStoreCropDir);
        }
    }

    /**
     * 预留黑色图片处理
     */
    public static void decode(){

    }


    public static void setRadioListener(IRadioImageCheckedListener radioListener){
        MediaGridFragment.iListenerRadio = radioListener;
    }
}
