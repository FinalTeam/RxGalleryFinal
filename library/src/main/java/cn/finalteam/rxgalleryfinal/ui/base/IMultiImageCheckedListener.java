package cn.finalteam.rxgalleryfinal.ui.base;

/**
 * 复选
 * Created by KARL on 2017-03-17 04-22-30.
 */
public interface IMultiImageCheckedListener {
    void selectedImg(Object t, boolean isChecked);

    void selectedImgMax(Object t, boolean isChecked, int maxSize);
}
