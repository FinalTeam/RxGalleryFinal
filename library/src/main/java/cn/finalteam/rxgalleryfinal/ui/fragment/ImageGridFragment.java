package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.component.DaggerMediaGridComponent;
import cn.finalteam.rxgalleryfinal.di.component.MediaGridComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.MediaGridModule;
import cn.finalteam.rxgalleryfinal.presenter.impl.MediaGridPresenterImpl;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaGridAdapter;
import cn.finalteam.rxgalleryfinal.ui.widget.RecycleViewDivider;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerViewFinal;
import cn.finalteam.rxgalleryfinal.view.MediaGridView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:02
 */
public class ImageGridFragment extends BaseFragment implements MediaGridView, RecyclerViewFinal.OnLoadMoreListener {

    private final int LIMIT = 20;

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

    private int mPage = 1;

    public static ImageGridFragment newInstance(){
        return new ImageGridFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRvMedia = (RecyclerViewFinal) view.findViewById(R.id.rv_media);
        mLlEmptyView = (LinearLayout) view.findViewById(R.id.ll_empty_view);
        mRvMedia.setEmptyView(mLlEmptyView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRvMedia.setLayoutManager(gridLayoutManager);
        mRvMedia.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));
        mMediaBeanList = new ArrayList<>();
        mMediaGridAdapter = new MediaGridAdapter(getContext(), mMediaBeanList, mScreenSize.widthPixels);
        mRvMedia.setAdapter(mMediaGridAdapter);

        mMediaGridPresenter.setMediaGridView(this);
        mMediaGridPresenter.getMediaList(mPage, LIMIT);
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
    public void showProgress() {
//        EmptyViewUtils.showLoading(mLlEmptyView);
    }

    @Override
    public void showEmptyView() {
//        EmptyViewUtils.showMessage(mLlEmptyView, "没有找到图片");
    }

    @Override
    public void loadMore() {
        mMediaGridPresenter.getMediaList(mPage, LIMIT);
    }

    @Override
    public void onRequestMediaCallback(List<MediaBean> list) {
        if(list != null && list.size() > 0) {
            mMediaBeanList.addAll(list);
            mMediaGridAdapter.notifyDataSetChanged();
        }
        Toast.makeText(getContext(), list.size() +"", Toast.LENGTH_SHORT).show();

        mPage++;

        if(list == null || list.size() < LIMIT) {
            mRvMedia.setHasLoadMore(false);
        } else {
            mRvMedia.setHasLoadMore(true);
        }

        mRvMedia.onLoadMoreComplete();
    }

}
