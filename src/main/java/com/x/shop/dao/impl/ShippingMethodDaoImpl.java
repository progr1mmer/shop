package com.x.shop.dao.impl;

import com.x.shop.dao.ShippingMethodDao;
import com.x.shop.entity.ShippingMethod;
import org.springframework.stereotype.Repository;

/**
 * Dao - 配送方式
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("shippingMethodDaoImpl")
public class ShippingMethodDaoImpl extends BaseDaoImpl<ShippingMethod, Long> implements ShippingMethodDao {

}