package com.x.shop.dao;

import com.x.shop.entity.DeliveryCenter;

/**
 * Dao - 发货点
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface DeliveryCenterDao extends BaseDao<DeliveryCenter, Long> {

    /**
     * 查找默认发货点
     *
     * @return 默认发货点，若不存在则返回null
     */
    DeliveryCenter findDefault();

}