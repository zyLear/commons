

package com.zylear.commons.util;

import com.zylear.commons.bean.Result;
import com.zylear.commons.exception.ResultMsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 返回结果处理
 */
public class ResultUtil {

    /**
     * 返回成功结果
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultMsg.SUCCESS.getCode());
        result.setMessage(ResultMsg.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 返回无数据成功结果
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 返回无数据错误结果
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(int code, String message) {
        return error(code, message, null, null);
    }

    public static <T> Result<T> error(int code, String message, String arg) {
        List<String> args = null;
        if(arg != null) {
            args = new ArrayList<>();
            args.add(arg);
        }
        return error(code, message, null, args);
    }

    public static <T> Result<T> error(int code, String message, List<String> args) {
        return error(code, message, null, args);
    }

    /**
     * 返回无数据错误结果
     * @param code
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(int code, String message, T data, List<String> args) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setArguments(args);
        result.setData(data);
        return result;
    }
}
