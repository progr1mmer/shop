package com.x.shop.service;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Promotion;

import java.util.List;

/**
 * Service - 促销
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface PromotionService extends BaseService<Promotion, Long> {

    /**
     * 查找促销
     *
     * @param hasBegun 是否已开始
     * @param hasEnded 是否已结束
     * @param count    数量
     * @param filters  筛选
     * @param orders   排序
     * @return 促销
     */
    List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders);

    /**
     * 查找促销(缓存)
     *
     * @param hasBegun    是否已开始
     * @param hasEnded    是否已结束
     * @param count       数量
     * @param filters     筛选
     * @param orders      排序
     * @param cacheRegion 缓存区域
     * @return 促销(缓存)
     */
    List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}