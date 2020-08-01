package com.x.shop.service.impl;

import com.x.shop.service.CacheService;
import com.x.shop.util.SettingUtils;
import freemarker.template.TemplateModelException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


/**
 * Service - 缓存
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("cacheServiceImpl")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private CacheManager cacheManager;
    /**
     * TODO ReloadableResourceBundleMessageSource
     */
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public String getDiskStorePath() {
        return cacheManager.getConfiguration().getDiskStoreConfiguration().getPath();
    }

    @Override
    public int getCacheSize() {
        int cacheSize = 0;
        String[] cacheNames = cacheManager.getCacheNames();
        if (cacheNames != null) {
            for (String cacheName : cacheNames) {
                Ehcache cache = cacheManager.getEhcache(cacheName);
                if (cache != null) {
                    cacheSize += cache.getSize();
                }
            }
        }
        return cacheSize;
    }

    @Override
    @CacheEvict(value = {"setting", "authorization", "logConfig", "template", "shipping", "area", "seo", "adPosition", "memberAttribute", "navigation", "tag", "friendLink", "brand", "article", "articleCategory", "product", "productCategory", "review", "consultation", "promotion"}, allEntries = true)
    public void clear() {
        //messageSource.clearCache();
        try {
            freeMarkerConfigurer.getConfiguration().setSharedVariable("setting", SettingUtils.get());
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        freeMarkerConfigurer.getConfiguration().clearTemplateCache();
    }

}