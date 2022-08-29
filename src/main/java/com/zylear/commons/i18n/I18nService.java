package com.zylear.commons.i18n;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 处理i18n服务类
 *
 * @author xiezongyu
 */
@Component
@Slf4j
public class I18nService {

    @Autowired
    private MessageSource messageSource;

    /**
     * 获取i18n信息
     *
     * @param bundle i18n的key
     * @param args   参数数组
     * @return i18n的信息，如找不到则返回bundle
     */
    public String getMessage(String bundle, Object[] args) {
        String message = bundle;
        try {
            message = messageSource.getMessage(bundle, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            log.warn("get i18n message fail. {}", message, e);
        }

        return message;
    }
}
