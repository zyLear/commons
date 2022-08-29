

package com.zylear.commons.exception;

import com.zylear.commons.bean.Result;
import com.zylear.commons.i18n.I18nService;
import com.zylear.commons.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;


@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @Autowired
    private I18nService i18nService;


    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Result> handleException(CommonException ex) {
        log.error("[error] CommonException catch", ex);
        return getResponseFromCodeAndBundle(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception ex) {
        log.error("[error] Exception catch", ex);
        return getResponseFromResult(ResultMsg.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleHttpMessageNotReadableException(Exception ex) throws Exception {
        //log.error("[error] HttpMessageNotReadableException catch", ex);
        throw ex;
    }

    /**
     * 参数绑定缺失异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result> handleLackParamException(Exception ex) {
        log.error("[error] MissingServletRequestParameterException catch", ex);
        return getResponseFromResult(ResultMsg.PARAMS_ERROR);
    }

    private ResponseEntity<Result> getResponseFromResult(ResultMsg result) {
        return getResponseFromCodeAndBundle(result.getCode(), result.getMessage(), null);
    }

    private ResponseEntity<Result> getResponseFromCodeAndBundle(int code, String bundle) {
        return getResponseFromCodeAndBundle(code, bundle, null);
    }

    private ResponseEntity<Result> getResponseFromCodeAndBundle(int code, String bundle, String[] args) {
        String message = i18nService.getMessage(bundle, args);
        List<String> argsList = null;
        if (args != null) {
            argsList = Arrays.asList(args);
        }
        Result<Object> result = ResultUtil.error(code, message, argsList);
        //HttpHeaders httpHeaders = HttpUtil.errorHttpHeader(ecode, message);
        // 如果是http标准错误返回码，直接使用
        if (code >= 400 && code < 600) {
            return new ResponseEntity<>(result, HttpStatus.valueOf(code));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
