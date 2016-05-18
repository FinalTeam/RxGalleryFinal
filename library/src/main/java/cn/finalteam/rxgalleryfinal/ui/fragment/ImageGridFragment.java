package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import javax.inject.Inject;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.di.component.DaggerMediaGridComponent;
import cn.finalteam.rxgalleryfinal.di.component.MediaGridComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.MediaGridModule;
import cn.finalteam.rxgalleryfinal.presenter.impl.MediaGridPresenterImpl;
import cn.finalteam.rxgalleryfinal.ui.widget.RecyclerViewFinal;
import cn.finalteam.rxgalleryfinal.utils.EmptyViewUtils;
import cn.finalteam.rxgalleryfinal.view.MediaGridView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:02
 */
public class ImageGridFragment extends BaseFragment implements MediaGridView {

    @Inject
    MediaGridPresenterImpl mMediaGridPresenter;

    @Inject
    Configuration mConfiguration;

    RecyclerViewFinal mRvMedia;
    LinearLayout mLlEmptyView;

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

        mMediaGridPresenter.setMediaGridView(this);
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
        EmptyViewUtils.showLoading(mLlEmptyView);
    }

    @Override
    public void showEmptyView() {
        EmptyViewUtils.showMessage(mLlEmptyView, "没有找到图片");
    }

}
