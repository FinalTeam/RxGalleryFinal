package cn.finalteam.rxgalleryfinal.ui.base;

/**
 * 单选裁剪
 * Created by KARL on 2017-05-31.
 */
public interface IRadioImageCheckedListener {

    /**
     * 裁剪之后
     * @param t
     */
    void cropAfter(Object t);

    /**
     * 返回true则关闭，false默认不关闭
     * @return
     */
    boolean isActivityFinish();
}
