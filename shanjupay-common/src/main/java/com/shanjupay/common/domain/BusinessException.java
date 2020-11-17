package com.shanjupay.common.domain;

/**
 * @Description: BusinessException
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/17 12:55
 * @since 0.1.0
 */
public class BusinessException extends RuntimeException{

    //错误代码
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BusinessException() {
        super();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
