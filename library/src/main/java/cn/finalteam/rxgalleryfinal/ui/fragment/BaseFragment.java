package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.utils.Logger;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 上午10:46
 */
public abstract class BaseFragment extends Fragment {

    private final String CLASS_NAME = getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        printFragmentLife("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printFragmentLife("onCreate");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupComponent(RxGalleryFinal.getRxGalleryFinalComponent());
        printFragmentLife("onViewCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        printFragmentLife("onCreateView");
        return inflater.inflate(getContentView(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        printFragmentLife("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        printFragmentLife("onStart");
        setTheme();
    }

    @Override
    public void onResume() {
        super.onResume();
        printFragmentLife("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        printFragmentLife("onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        printFragmentLife("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        printFragmentLife("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        printFragmentLife("onDetach");
    }

    public abstract int getContentView();

    protected abstract void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent);

    public void setTheme(){}


    private void printFragmentLife(String method){
        Logger.i(String.format("Fragment:%s Method:%s", CLASS_NAME, method));
    }

}
