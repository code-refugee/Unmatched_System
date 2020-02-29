package com.unmatched.common.messageTransform.exception;

public class MessageConverterException extends RuntimeException {

    private String errMsg;

    public MessageConverterException(String errMsg){
        super(errMsg);
        this.errMsg=errMsg;
    }
}
