package com.x.shop.dao;

import com.x.shop.entity.Seo;

/**
 * Dao - SEO设置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface SeoDao extends BaseDao<Seo, Long> {

    /**
     * 查找SEO设置
     *
     * @param type 类型
     * @return SEO设置
     */
    Seo find(Seo.Type type);

}