package com.x.shop.dao.impl;

import com.x.shop.dao.PaymentDao;
import com.x.shop.entity.Payment;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

/**
 * Dao - 收款单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("paymentDaoImpl")
public class PaymentDaoImpl extends BaseDaoImpl<Payment, Long> implements PaymentDao {

    @Override
    public Payment findBySn(String sn) {
        if (sn == null) {
            return null;
        }
        String jpql = "select payment from Payment payment where lower(payment.sn) = lower(:sn)";
        try {
            return entityManager.createQuery(jpql, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}