package cn.finalteam.rxgalleryfinal.view;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.BucketBean;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 上午11:00
 */
public interface MediaGridView {
    void onRequestMediaCallback(List<MediaBean> list);

    void onRequestBucketCallback(List<BucketBean> list);
}
