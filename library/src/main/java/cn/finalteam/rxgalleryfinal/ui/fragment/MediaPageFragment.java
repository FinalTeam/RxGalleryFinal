package cn.finalteam.rxgalleryfinal.ui.fragment;

import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 下午10:02
 */
public class MediaPageFragment extends BaseFragment {
    public static MediaPageFragment newInstance(){
        return new MediaPageFragment();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_media_grid;
    }

    @Override
    protected void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent) {

    }
}
