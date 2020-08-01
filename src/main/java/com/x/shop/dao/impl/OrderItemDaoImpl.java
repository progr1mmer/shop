package com.x.shop.dao.impl;

import com.x.shop.dao.OrderItemDao;
import com.x.shop.entity.OrderItem;
import org.springframework.stereotype.Repository;

/**
 * Dao - 订单项
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("orderItemDaoImpl")
public class OrderItemDaoImpl extends BaseDaoImpl<OrderItem, Long> implements OrderItemDao {

}