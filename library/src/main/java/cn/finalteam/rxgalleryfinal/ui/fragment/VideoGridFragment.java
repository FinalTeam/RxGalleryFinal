package cn.finalteam.rxgalleryfinal.ui.fragment;

import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:02
 */
public class VideoGridFragment extends BaseFragment {

    public static VideoGridFragment newInstance(){
        return new VideoGridFragment();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_media_grid;
    }

    @Override
    protected void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent) {

    }
}
