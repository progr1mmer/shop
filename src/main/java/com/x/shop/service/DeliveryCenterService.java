package com.x.shop.service;

import com.x.shop.entity.DeliveryCenter;

/**
 * Service - 发货点
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface DeliveryCenterService extends BaseService<DeliveryCenter, Long> {

    /**
     * 查找默认发货点
     *
     * @return 默认发货点，若不存在则返回null
     */
    DeliveryCenter findDefault();

}