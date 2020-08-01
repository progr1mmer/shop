package com.x.shop.dao;

import com.x.shop.entity.DeliveryTemplate;

/**
 * Dao - 快递单模板
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface DeliveryTemplateDao extends BaseDao<DeliveryTemplate, Long> {

    /**
     * 查找默认快递单模板
     *
     * @return 默认快递单模板，若不存在则返回null
     */
    DeliveryTemplate findDefault();

}