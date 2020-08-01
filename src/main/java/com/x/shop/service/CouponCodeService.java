package com.x.shop.service;

import com.x.shop.entity.Coupon;
import com.x.shop.entity.CouponCode;

import java.util.List;

/**
 * Service - 优惠码
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface CouponCodeService extends BaseService<CouponCode, Long> {

    /**
     * 判断优惠码是否存在
     *
     * @param code 号码(忽略大小写)
     * @return 优惠码是否存在
     */
    boolean codeExists(String code);

    /**
     * 根据号码查找优惠码
     *
     * @param code 号码(忽略大小写)
     * @return 优惠码，若不存在则返回null
     */
    CouponCode findByCode(String code);

    /**
     * 生成优惠码
     *
     * @param coupon 优惠券
     * @return 优惠码
     */
    CouponCode build(Coupon coupon);

    /**
     * 生成优惠码
     *
     * @param coupon 优惠券
     * @param count  数量
     * @return 优惠码
     */
    List<CouponCode> build(Coupon coupon, Integer count);

    /**
     * 查找优惠码数量
     *
     * @param coupon     优惠券
     * @param hasBegun   是否已开始
     * @param hasExpired 是否已过期
     * @param isUsed     是否已使用
     * @return 优惠码数量
     */
    Long count(Coupon coupon, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);

}