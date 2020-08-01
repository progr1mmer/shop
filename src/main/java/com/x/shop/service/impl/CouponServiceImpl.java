package com.x.shop.service.impl;

import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.CouponDao;
import com.x.shop.entity.Coupon;
import com.x.shop.service.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 优惠券
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("couponServiceImpl")
public class CouponServiceImpl extends BaseServiceImpl<Coupon, Long> implements CouponService {

    @Resource(name = "couponDaoImpl")
    private CouponDao couponDao;

    @Override
    @Transactional(readOnly = true)
    public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
        return couponDao.findPage(isEnabled, isExchange, hasExpired, pageable);
    }

}