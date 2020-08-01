package com.x.shop.service.impl;

import com.x.shop.dao.CouponCodeDao;
import com.x.shop.entity.Coupon;
import com.x.shop.entity.CouponCode;
import com.x.shop.service.CouponCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 优惠码
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("couponCodeServiceImpl")
public class CouponCodeServiceImpl extends BaseServiceImpl<CouponCode, Long> implements CouponCodeService {

    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;

    @Override
    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return couponCodeDao.codeExists(code);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponCode findByCode(String code) {
        return couponCodeDao.findByCode(code);
    }

    @Override
    public CouponCode build(Coupon coupon) {
        return couponCodeDao.build(coupon);
    }

    @Override
    public List<CouponCode> build(Coupon coupon, Integer count) {
        return couponCodeDao.build(coupon, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Long count(Coupon coupon, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
        return couponCodeDao.count(coupon, hasBegun, hasExpired, isUsed);
    }

}