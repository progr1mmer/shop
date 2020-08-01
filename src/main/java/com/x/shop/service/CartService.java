package com.x.shop.service;

import com.x.shop.entity.Cart;

/**
 * Service - 购物车
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface CartService extends BaseService<Cart, Long> {

    /**
     * 获取当前购物车
     *
     * @return 当前购物车, 若不存在则返回null
     */
    Cart getCurrent();

    /**
     * 合并临时购物车至会员
     *
     * @param cart   临时购物车
     */
    void merge(Cart cart);

    /**
     * 清除过期购物车
     */
    void evictExpired();

}