package com.x.shop.dao.impl;

import com.x.shop.dao.ShippingDao;
import com.x.shop.entity.Shipping;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

/**
 * Dao - 发货单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("shippingDaoImpl")
public class ShippingDaoImpl extends BaseDaoImpl<Shipping, Long> implements ShippingDao {

    @Override
    public Shipping findBySn(String sn) {
        if (sn == null) {
            return null;
        }
        String jpql = "select shipping from Shipping shipping where lower(shipping.sn) = lower(:sn)";
        try {
            return entityManager.createQuery(jpql, Shipping.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}