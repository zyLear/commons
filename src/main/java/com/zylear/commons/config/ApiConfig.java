package com.zylear.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * web拦截器配置
 */
@Configuration
public class ApiConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private LocaleChangeInterceptor localeInterceptor;

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Autowired
    private LogInterceptor logInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. Common, i18N
        registry.addInterceptor(localeInterceptor);
        // 2. All, Request Filter
        registry.addInterceptor(requestInterceptor).addPathPatterns("/v1/app/*");
        // 3. All, log Filter
        registry.addInterceptor(logInterceptor);


    }
}
