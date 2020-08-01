package com.x.shop.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author progr1mmer.
 * @date Created on 2020/2/12.
 */
@Configuration
public class WebMvcInterceptorConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration executeTimeInterceptor = registry.addInterceptor(executeTimeInterceptor());
        executeTimeInterceptor.addPathPatterns("/admin/**");

        InterceptorRegistration listInterceptor = registry.addInterceptor(listInterceptor());
        listInterceptor.addPathPatterns("/admin/**");

        InterceptorRegistration logInterceptor = registry.addInterceptor(logInterceptor());
        logInterceptor.addPathPatterns("/admin/**");

        InterceptorRegistration tokenInterceptor = registry.addInterceptor(tokenInterceptor());
        tokenInterceptor.addPathPatterns("/**");
        tokenInterceptor.excludePathPatterns("/payment/notify/**");
    }

    @Bean
    ExecuteTimeInterceptor executeTimeInterceptor() {
        return new ExecuteTimeInterceptor();
    }

    @Bean
    ListInterceptor listInterceptor() {
        return new ListInterceptor();
    }

    @Bean
    LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }
}
