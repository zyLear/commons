package com.zylear.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author xiezongyu
 * @date 2021/4/7
 */
@SpringBootApplication
public class CommonApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(CommonApplication.class);
}
}