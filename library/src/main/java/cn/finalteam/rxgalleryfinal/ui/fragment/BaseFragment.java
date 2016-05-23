package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 上午10:46
 */
public abstract class BaseFragment extends Fragment {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupComponent(RxGalleryFinal.getRxGalleryFinalComponent());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getContentView(), container, false);
    }

    public abstract int getContentView();

    protected abstract void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent);

}
