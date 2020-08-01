package com.x.shop.dao;

import com.x.shop.entity.Shipping;

/**
 * Dao - 发货单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ShippingDao extends BaseDao<Shipping, Long> {

    /**
     * 根据编号查找发货单
     *
     * @param sn 编号(忽略大小写)
     * @return 若不存在则返回null
     */
    Shipping findBySn(String sn);

}