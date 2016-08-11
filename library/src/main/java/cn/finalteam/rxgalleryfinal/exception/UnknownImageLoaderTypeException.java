package cn.finalteam.rxgalleryfinal.exception;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/8/9 上午7:50
 */
public class UnknownImageLoaderTypeException extends RuntimeException {

    public UnknownImageLoaderTypeException(){
        super("未知的ImageLoader");
    }

    public UnknownImageLoaderTypeException(String errormsg){
        super(errormsg);
    }
}
