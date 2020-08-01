package com.x.shop.dao.impl;

import com.x.shop.dao.PaymentMethodDao;
import com.x.shop.entity.PaymentMethod;
import org.springframework.stereotype.Repository;

/**
 * Dao - 支付方式
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("paymentMethodDaoImpl")
public class PaymentMethodDaoImpl extends BaseDaoImpl<PaymentMethod, Long> implements PaymentMethodDao {

}