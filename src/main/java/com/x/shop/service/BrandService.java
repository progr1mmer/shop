package com.x.shop.service;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Brand;

import java.util.List;

/**
 * Service - 品牌
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface BrandService extends BaseService<Brand, Long> {

    /**
     * 查找品牌(缓存)
     *
     * @param count       数量
     * @param filters     筛选
     * @param orders      排序
     * @param cacheRegion 缓存区域
     * @return 品牌(缓存)
     */
    List<Brand> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}