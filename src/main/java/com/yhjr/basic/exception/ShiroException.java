package com.yhjr.basic.exception;

/**
 * 自定义异常
 */
public class ShiroException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ShiroException(String erroeCode, String message) {
        super(erroeCode, message);
    }

    public ShiroException(String message) {
        super(AbsErrorCodeConstant.ERROR,message);
    }

    public ShiroException(String erroeCode, Throwable throwable) {
        super(erroeCode,null, throwable);
    }

    public ShiroException(String erroeCode, String msg,Throwable e) {
        super(erroeCode, msg,e);
    }

}
