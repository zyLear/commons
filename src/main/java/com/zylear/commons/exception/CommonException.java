

package com.zylear.commons.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author xiezongyu
 */
public class CommonException extends NestedRuntimeException {
    
    private static final long serialVersionUID = 4734860024944705912L;
    
    private int code;

    public int getCode() {
        return code;
    }

    public CommonException(ResultMsg resultMsg) {
        super(resultMsg.getMessage());
        this.code = resultMsg.getCode();
    }
    public CommonException(String msg, int code) {
        super(msg);
        this.code = code;
    }
}