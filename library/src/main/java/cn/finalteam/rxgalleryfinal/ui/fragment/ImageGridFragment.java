package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.app.Activity;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.anim.Animation;
import cn.finalteam.rxgalleryfinal.anim.SlideInUnderneathAnimation;
import cn.finalteam.rxgalleryfinal.anim.SlideOutUnderneathAnimation;
import cn.finalteam.rxgalleryfinal.bean.BucketBean;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.component.DaggerMediaGridComponent;
import cn.finalteam.rxgalleryfinal.di.component.MediaGridComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.MediaGridModule;
import cn.finalteam.rxgalleryfinal.presenter.impl.MediaGridPresenterImpl;
import cn.finalteam.rxgalleryfinal.ui.adapter.BucketAdapter;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaGridAdapter;
import cn.finalteam.rxgalleryfinal.ui.widget.FooterAdapter;
import cn.finalteam.rxgalleryfinal.ui.widget.HorizontalDividerItemDecoration;
import cn.finalteam.rxgalleryfinal.ui.widget.MarginDecoration;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerViewFinal;
import cn.finalteam.rxgalleryfinal.utils.CameraUtils;
import cn.finalteam.rxgalleryfinal.utils.EmptyViewUtils;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;
import cn.finalteam.rxgalleryfinal.utils.MediaUtils;
import cn.finalteam.rxgalleryfinal.view.MediaGridView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:02
 */
public class ImageGridFragment extends BaseFragment implements MediaGridView, RecyclerViewFinal.OnLoadMoreListener,
        FooterAdapter.OnItemClickListener,View.OnClickListener, MediaScanner.ScanCallback, BucketAdapter.OnRecyclerViewItemClickListener {

    private final String IMAGE_STORE_FILE_NAME = "IMG_%s.jpg";
    private final int TAKE_IMAGE_REQUEST_CODE = 1001;

    private final String TAKE_URL_STORAGE_KEY = "take_url_storage_key";
    private final String BUCKET_ID_KEY = "bucket_id_key";

    private final int LIMIT = 23;

    @Inject
    MediaGridPresenterImpl mMediaGridPresenter;
    @Inject
    Configuration mConfiguration;
    @Inject
    DisplayMetrics mScreenSize;

    private List<MediaBean> mMediaBeanList;
    MediaGridAdapter mMediaGridAdapter;
    RecyclerViewFinal mRvMedia;
    LinearLayout mLlEmptyView;
    RecyclerView mRvBucket;
    BucketAdapter mBucketAdapter;
    RelativeLayout mRlBucektOverview;
    List<BucketBean> mBucketBeanList;
    TextView mTvFolderName;
    TextView mTvPreview;
    MediaScanner mMediaScanner;

    private int mPage = 1;
    private File mImageStoreDir;
    private String mImagePath;

    private String mBucketId = String.valueOf(Integer.MIN_VALUE);

    public static ImageGridFragment newInstance() {
        return new ImageGridFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/RxGalleryFinal/");
        if (!mImageStoreDir.exists()) {
            mImageStoreDir.mkdirs();
        }
        mMediaScanner = new MediaScanner(context);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_media_grid;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRvMedia = (RecyclerViewFinal) view.findViewById(R.id.rv_media);
        mLlEmptyView = (LinearLayout) view.findViewById(R.id.ll_empty_view);
        mRvBucket = (RecyclerView) view.findViewById(R.id.rv_bucket);
        mRlBucektOverview = (RelativeLayout) view.findViewById(R.id.rl_bucket_overview);

        mRvMedia.setEmptyView(mLlEmptyView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRvMedia.addItemDecoration(new MarginDecoration(getContext()));
        mRvMedia.setLayoutManager(gridLayoutManager);
        mRvMedia.setOnLoadMoreListener(this);
        mRvMedia.setFooterViewHide(true);

        if(mConfiguration.getPauseOnScrollListener() != null) {
            mRvMedia.addOnScrollListener(mConfiguration.getPauseOnScrollListener());
            mRvBucket.addOnScrollListener(mConfiguration.getPauseOnScrollListener());
        }

        mTvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
        mTvFolderName.setOnClickListener(this);
        mTvPreview = (TextView) view.findViewById(R.id.tv_preview);
        mTvPreview.setOnClickListener(this);

        mMediaBeanList = new ArrayList<>();
        mMediaGridAdapter = new MediaGridAdapter(getContext(), mMediaBeanList, mScreenSize.widthPixels, mConfiguration);
        mRvMedia.setAdapter(mMediaGridAdapter);

        mMediaGridPresenter.setMediaGridView(this);
        mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRvBucket.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(getResources().getColor(R.color.C7C7C7))
                .size(getResources().getDimensionPixelSize(R.dimen.divider_decoration_height))
                .margin(getResources().getDimensionPixelSize(R.dimen.bucket_margin),
                        getResources().getDimensionPixelSize(R.dimen.bucket_margin))
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
    }

    @Override
    protected void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent) {
        MediaGridComponent mediaGridComponent = DaggerMediaGridComponent.builder()
                .rxGalleryFinalComponent(RxGalleryFinal.getRxGalleryFinalComponent())
                .mediaGridModule(new MediaGridModule(getContext(), true))
                .build();
        mediaGridComponent.inject(this);
    }

    @Override
    public void loadMore() {
        mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
    }

    @Override
    public void onRequestMediaCallback(List<MediaBean> list) {
        if (mPage == 1 && TextUtils.equals(mBucketId, String.valueOf(Integer.MIN_VALUE))) {
            MediaBean takePhotoBean = new MediaBean();
            takePhotoBean.setId(Integer.MIN_VALUE);
            takePhotoBean.setBucketId(String.valueOf(Integer.MIN_VALUE));
            mMediaBeanList.add(takePhotoBean);
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
            EmptyViewUtils.showMessage(mLlEmptyView, "空空如也");
        }

        mRvMedia.onLoadMoreComplete();
    }

    @Override
    public void onRequestBucketCallback(List<BucketBean> list) {
        if(list == null){
            return;
        }

        mBucketBeanList.addAll(list);
        mMediaGridAdapter.notifyDataSetChanged();
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

        mRvMedia.setFooterViewHide(true);
        mPage = 1;
        mMediaGridPresenter.getMediaList(mBucketId, mPage, LIMIT);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {

            if (!CameraUtils.hasCamera(getContext())) {
                Toast.makeText(getContext(), "该设备无摄像头", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (captureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                String filename = String.format(IMAGE_STORE_FILE_NAME, dateFormat.format(new Date()));
                mImagePath = new File(mImageStoreDir, filename).getAbsolutePath();
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImagePath)));
                startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
            } else {
                Toast.makeText(getContext(), "相机不可用", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Logger.i(String.format("拍照成功,图片存储路径:%s", mImagePath));

            //刷新相册数据库
            mMediaScanner.scanFile(mImagePath, "image/jpeg", this);
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

        } else if(id == R.id.tv_folder_name) {
            v.setEnabled(false);
            int visibility = mRlBucektOverview.getVisibility();
            if(visibility == View.VISIBLE) {
                new SlideOutUnderneathAnimation(mRvBucket)
                        .setDirection(Animation.DIRECTION_DOWN)
                        .setListener(animation -> {
                            v.setEnabled(true);
                            mRlBucektOverview.setVisibility(View.GONE);
                        })
                        .animate();
            } else  {
                mRlBucektOverview.setVisibility(View.VISIBLE);
                new SlideInUnderneathAnimation(mRvBucket)
                        .setDirection(Animation.DIRECTION_DOWN)
                        .setListener(animation -> {
                            v.setEnabled(true);
                        })
                        .animate();
            }
        }
    }

    @Override
    public void onScanCompleted(String[] images) {
        if(images == null || images.length == 0){
            Logger.i("images empty");
            return;
        }

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
                if(!isDetached() && mediaBean != null) {
                    mMediaBeanList.add(1, mediaBean);
                    mMediaGridAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
