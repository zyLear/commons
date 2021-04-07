package com.zylear.commons.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestObject {

    /**
     * 请求开始时间
     */
    private Long start = System.currentTimeMillis();

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 请求唯一id
     */
    private String requestId;

    public static Long DEFAULT_VALUE_LONG = 0L;

    public static boolean isDefaultValueLong(Long value) {
        if (value != null) {
            return DEFAULT_VALUE_LONG.equals(value);
        }
        return false;
    }
}
