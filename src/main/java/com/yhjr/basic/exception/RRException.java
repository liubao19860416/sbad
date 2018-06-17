package com.yhjr.basic.exception;

/**
 * 自定义异常
 */
public class RRException extends BaseException {
    private static final long serialVersionUID = 1L;

    public RRException(String erroeCode, String message) {
        super(erroeCode, message);
    }

    public RRException(String message) {
        super(AbsErrorCodeConstant.ERROR,message);
    }

    public RRException(String erroeCode, Throwable throwable) {
        super(erroeCode,null, throwable);
    }

    public RRException(String erroeCode, String msg,Throwable e) {
        super(erroeCode, msg,e);
    }

}
