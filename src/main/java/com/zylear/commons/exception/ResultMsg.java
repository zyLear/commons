package com.zylear.commons.exception;

import lombok.Data;

/**
 * @author xiezongyu
 */

public enum ResultMsg {

    SUCCESS(0, "ok"),
    INTERNAL_SERVER_ERROR(1000, "internal error"),
    PARAMS_ERROR(1001, "params error"),
    UNAUTHORIZED(1002, "unauthorized");

    ;

    private final int code;
    private final String message;


    ResultMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
