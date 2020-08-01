package com.x.shop.dao.impl;

import com.x.shop.dao.CartDao;
import com.x.shop.entity.Cart;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import java.util.Date;

/**
 * Dao - 购物车
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("cartDaoImpl")
public class CartDaoImpl extends BaseDaoImpl<Cart, Long> implements CartDao {

    @Override
    public void evictExpired() {
        String jpql = "delete from Cart cart where cart.modifyDate <= :expire";
        entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("expire", DateUtils.addSeconds(new Date(), -Cart.TIMEOUT)).executeUpdate();
    }

}