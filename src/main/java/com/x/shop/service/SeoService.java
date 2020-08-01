package com.x.shop.service;

import com.x.shop.entity.Seo;

/**
 * Service - SEO设置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface SeoService extends BaseService<Seo, Long> {

    /**
     * 查找SEO设置
     *
     * @param type 类型
     * @return SEO设置
     */
    Seo find(Seo.Type type);

    /**
     * 查找SEO设置(缓存)
     *
     * @param type        类型
     * @param cacheRegion 缓存区域
     * @return SEO设置(缓存)
     */
    Seo find(Seo.Type type, String cacheRegion);

}