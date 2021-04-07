package com.zylear.commons.config;

import com.zylear.commons.bean.RequestHolder;
import com.zylear.commons.bean.RequestObject;
import com.zylear.commons.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 日志标签
 */
@Component
@Slf4j
public class LogInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestObject requestObject = RequestHolder.get();
        if (requestObject != null) {
            MDC.put(AppConstants.USER_ID, String.valueOf(requestObject.getUserId()));
            MDC.put(AppConstants.GUID, requestObject.getRequestId());
        }else {
            MDC.put(AppConstants.GUID, UUID.randomUUID().toString());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(AppConstants.USER_ID);
        MDC.remove(AppConstants.GUID);
    }
}
