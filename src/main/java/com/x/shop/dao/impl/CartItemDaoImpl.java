package com.x.shop.dao.impl;

import com.x.shop.dao.CartItemDao;
import com.x.shop.entity.CartItem;
import org.springframework.stereotype.Repository;

/**
 * Dao - 购物车项
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("cartItemDaoImpl")
public class CartItemDaoImpl extends BaseDaoImpl<CartItem, Long> implements CartItemDao {

}