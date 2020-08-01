package com.x.shop.dao.impl;

import com.x.shop.dao.RefundsDao;
import com.x.shop.entity.Refunds;
import org.springframework.stereotype.Repository;

/**
 * Dao - 退款单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("refundsDaoImpl")
public class RefundsDaoImpl extends BaseDaoImpl<Refunds, Long> implements RefundsDao {

}