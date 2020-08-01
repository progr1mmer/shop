package com.x.shop.service;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Navigation;

import java.util.List;

/**
 * Service - 导航
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface NavigationService extends BaseService<Navigation, Long> {

    /**
     * 查找导航
     *
     * @param position 位置
     * @return 导航
     */
    List<Navigation> findList(Navigation.Position position);

    /**
     * 查找导航(缓存)
     *
     * @param count       数量
     * @param filters     筛选
     * @param orders      排序
     * @param cacheRegion 缓存区域
     * @return 导航(缓存)
     */
    List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}