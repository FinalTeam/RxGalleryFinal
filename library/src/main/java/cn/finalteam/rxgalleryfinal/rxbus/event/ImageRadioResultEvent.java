package cn.finalteam.rxgalleryfinal.rxbus.event;

import cn.finalteam.rxgalleryfinal.bean.ImageCropBean;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/8/1 下午10:49
 */
public class ImageRadioResultEvent implements BaseResultEvent {
    private final ImageCropBean resultBean;

    public ImageRadioResultEvent(ImageCropBean bean) {
        this.resultBean = bean;
    }

    public ImageCropBean getResult() {
        return resultBean;
    }

}
