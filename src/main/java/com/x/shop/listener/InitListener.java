package com.x.shop.listener;

import java.io.File;
import java.util.logging.Logger;

import javax.annotation.Resource;

import com.x.shop.service.CacheService;
import com.x.shop.service.SearchService;
import com.x.shop.service.StaticService;

import com.x.shop.util.ContextPathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Listener - 初始化
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 安装初始化配置文件
     */
    private static final String INSTALL_INIT_CONFIG_FILE_PATH = "install_init.conf";

    /**
     * logger
     */
    private static final Logger logger = Logger.getLogger(InitListener.class.getName());

    @Value("${spring.application.name:SHOP}")
    private String appName;
    @Resource(name = "staticServiceImpl")
    private StaticService staticService;
    @Resource(name = "cacheServiceImpl")
    private CacheService cacheService;
    @Resource(name = "searchServiceImpl")
    private SearchService searchService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            System.setProperty("system.project_name", appName);
            String info = "Initializing " + appName.toUpperCase();
            logger.info(info);
            File installInitConfigFile = new File(ContextPathUtils.getClassPath() + INSTALL_INIT_CONFIG_FILE_PATH);
            if (installInitConfigFile.exists()) {
                cacheService.clear();
                staticService.buildAll();
                searchService.purge();
                searchService.index();
                installInitConfigFile.delete();
            } else {
                //staticService.buildIndex();
                staticService.buildOther();
            }
        }
    }

}