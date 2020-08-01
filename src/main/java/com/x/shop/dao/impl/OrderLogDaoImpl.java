package com.x.shop.dao.impl;

import com.x.shop.dao.OrderLogDao;
import com.x.shop.entity.OrderLog;
import org.springframework.stereotype.Repository;

/**
 * Dao - 订单日志
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("orderLogDaoImpl")
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog, Long> implements OrderLogDao {

}