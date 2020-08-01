package com.x.shop.dao;

import com.x.shop.entity.Cart;

/**
 * Dao - 购物车
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface CartDao extends BaseDao<Cart, Long> {

    /**
     * 清除过期购物车
     */
    void evictExpired();

}