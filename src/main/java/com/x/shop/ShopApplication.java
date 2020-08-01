package com.x.shop;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author progr1mmer
 * @date Created on 2020/2/3
 */
@SpringBootApplication
@ComponentScan(lazyInit = true)
@ServletComponentScan
@EnableCaching
@PropertySource("classpath:shop.properties")
public class ShopApplication extends SpringBootServletInitializer {

    @Value("${task.core_pool_size}")
    private int corePoolSize;
    @Value("${task.max_pool_size}")
    private int maxPoolSize;
    @Value("${task.queue_capacity}")
    private int queueCapacity;
    @Value("${task.keep_alive_seconds}")
    private int keepAliveSeconds;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShopApplication.class);
    }

    @Bean
    LocaleResolver localeResolver() {
        return new LocaleResolver() {

            @Override
            public Locale resolveLocale(HttpServletRequest httpServletRequest) {
                String l = httpServletRequest.getParameter("l");
                Locale locale = httpServletRequest.getLocale();
                if (!StringUtils.isEmpty(l)) {
                    String[] split = l.split("_");
                    locale = new Locale(split[0], split[1]);
                }
                return locale;
            }

            @Override
            public void setLocale(HttpServletRequest httpServletRequest, @Nullable HttpServletResponse httpServletResponse, @Nullable Locale locale) {

            }
        };
    }

    @Bean
    TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        return threadPoolTaskExecutor;
    }

}
