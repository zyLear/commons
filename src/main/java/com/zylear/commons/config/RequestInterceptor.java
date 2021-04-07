package com.zylear.commons.config;

import com.zylear.commons.bean.RequestHolder;
import com.zylear.commons.bean.RequestObject;
import com.zylear.commons.constant.AppConstants;
import com.zylear.commons.exception.CommonException;
import com.zylear.commons.exception.ResultMsg;
import com.zylear.commons.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 从请求头获取token，解析成RequestObject
 */
@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = UUID.randomUUID().toString();
        MDC.put(AppConstants.GUID, requestId);
        log.info("[request start] {} {}, headers:{}", request.getMethod(), request.getRequestURI(),
                getRequestHeaders(request));
        String token = request.getHeader(AppConstants.AUTHORIZATION);
        if (token == null) {
            log.warn("token is empty");
            throw new CommonException(ResultMsg.UNAUTHORIZED);
        }
        Map<String, Object> body;
        try {
            body = JwtUtil.parse(token);
        } catch (ExpiredJwtException e) {
            log.warn("token expire. ", e);
            throw new CommonException(ResultMsg.UNAUTHORIZED);
        }
        // token expire
        Integer userId = (Integer) body.get(AppConstants.USER_ID);
        String issuer = (String) body.get(AppConstants.ISSUER);
        if (userId == null || issuer == null) {
            log.warn("token error");
            throw new CommonException(ResultMsg.UNAUTHORIZED);
        }
        RequestObject requestObject = new RequestObject();
        requestObject.setUserId((long) userId);
        requestObject.setRequestId(requestId);
        RequestHolder.set(requestObject);

        log.info("[request start] {} {}, requestObject:{}", request.getMethod(), request.getRequestURI(),
                requestObject);

        return true;
    }

    protected final Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new LinkedHashMap<String, Object>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, getHeaderValue(request, name));
        }
        return headers;
    }

    protected final Object getHeaderValue(HttpServletRequest request, String name) {
        return getHeaderValue(Collections.list(request.getHeaders(name)));
    }

    protected final Object getHeaderValue(List<String> value) {
        if (value.size() == 1) {
            return value.get(0);
        }
        if (value.isEmpty()) {
            return "";
        }
        return value;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RequestObject requestObject = RequestHolder.get();
        log.info("[request   end] {} {} status:{}, cost:{} ms, request object:{}", request.getMethod(),
                response.getStatus(), request.getRequestURI(),
                (System.currentTimeMillis() - requestObject.getStart()), requestObject);
        RequestHolder.clear();
        MDC.remove(AppConstants.GUID);
    }

}
