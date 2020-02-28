package com.unmatched.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "不支持的操作！")
public class BaseRootException extends RuntimeException{
    private String message;

    public BaseRootException(String message) {
        super(message);
        this.message = message;
    }
}
