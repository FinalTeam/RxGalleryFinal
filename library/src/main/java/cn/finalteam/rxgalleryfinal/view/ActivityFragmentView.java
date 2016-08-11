package cn.finalteam.rxgalleryfinal.view;

import java.util.ArrayList;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 下午9:56
 */
public interface ActivityFragmentView {

    void showMediaGridFragment();
    void showMediaPageFragment(ArrayList<MediaBean> list, int position);
    void showMediaPreviewFragment();
}
