package com.x.shop.service;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Tag;

import java.util.List;

/**
 * Service - 标签
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface TagService extends BaseService<Tag, Long> {

    /**
     * 查找标签
     *
     * @param type 类型
     * @return 标签
     */
    List<Tag> findList(Tag.Type type);

    /**
     * 查找标签(缓存)
     *
     * @param count       数量
     * @param filters     筛选
     * @param orders      排序
     * @param cacheRegion 缓存区域
     * @return 标签(缓存)
     */
    List<Tag> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}