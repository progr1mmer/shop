package com.x.shop.service;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.FriendLink;
import com.x.shop.entity.FriendLink.Type;

import java.util.List;

/**
 * Service - 友情链接
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface FriendLinkService extends BaseService<FriendLink, Long> {

    /**
     * 查找友情链接
     *
     * @param type 类型
     * @return 友情链接
     */
    List<FriendLink> findList(Type type);

    /**
     * 查找友情链接(缓存)
     *
     * @param count       数量
     * @param filters     筛选
     * @param orders      排序
     * @param cacheRegion 缓存区域
     * @return 友情链接(缓存)
     */
    List<FriendLink> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

}