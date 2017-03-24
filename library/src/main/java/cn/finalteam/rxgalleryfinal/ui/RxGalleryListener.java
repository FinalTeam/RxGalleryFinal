package cn.finalteam.rxgalleryfinal.ui;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaGridAdapter;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;

/**
 * Created by KARL on 2017-03-17 04-42-25.
 */
public class RxGalleryListener {

    private RxGalleryListener(){}

    private static RxGalleryListener rxGalleryListener;

    public static RxGalleryListener getInstance(){
        if(rxGalleryListener == null){
            rxGalleryListener = new RxGalleryListener();
        }
        return rxGalleryListener;
    }

    /**
     * 图片多选的事件
     */
    public void setMultiImageCheckedListener(IMultiImageCheckedListener checkedImageListener){
        MediaGridAdapter.setCheckedListener(checkedImageListener);
    }
}
