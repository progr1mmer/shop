package com.x.shop.service.impl;

import com.x.shop.entity.OrderLog;
import com.x.shop.service.OrderLogService;
import org.springframework.stereotype.Service;

/**
 * Service - 订单日志
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("orderLogServiceImpl")
public class OrderLogServiceImpl extends BaseServiceImpl<OrderLog, Long> implements OrderLogService {

}