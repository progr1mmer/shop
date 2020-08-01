package com.x.shop.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity - 优惠码
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_coupon_code")
public class CouponCode extends BaseEntity {

    private static final long serialVersionUID = -1812874037224306719L;

    /**
     * 号码
     */
    private String code;

    /**
     * 是否已使用
     */
    private Boolean isUsed;

    /**
     * 使用日期
     */
    private Date usedDate;

    /**
     * 优惠券
     */
    private Coupon coupon;

    /**
     * 订单
     */
    private Order order;

    /**
     * 获取号码
     *
     * @return 号码
     */
    @Column(nullable = false, updatable = false, unique = true, length = 100)
    public String getCode() {
        return code;
    }

    /**
     * 设置号码
     *
     * @param code 号码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取是否已使用
     *
     * @return 是否已使用
     */
    @Column(nullable = false)
    public Boolean getIsUsed() {
        return isUsed;
    }

    /**
     * 设置是否已使用
     *
     * @param isUsed 是否已使用
     */
    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    /**
     * 获取使用日期
     *
     * @return 使用日期
     */
    public Date getUsedDate() {
        return usedDate;
    }

    /**
     * 设置使用日期
     *
     * @param usedDate 使用日期
     */
    public void setUsedDate(Date usedDate) {
        this.usedDate = usedDate;
    }

    /**
     * 获取优惠券
     *
     * @return 优惠券
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public Coupon getCoupon() {
        return coupon;
    }

    /**
     * 设置优惠券
     *
     * @param coupon 优惠券
     */
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    /**
     * 获取订单
     *
     * @return 订单
     */
    @OneToOne(mappedBy = "couponCode", fetch = FetchType.LAZY)
    public Order getOrder() {
        return order;
    }

    /**
     * 设置订单
     *
     * @param order 订单
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 删除前处理
     */
    @PreRemove
    public void preRemove() {
        if (getOrder() != null) {
            getOrder().setCouponCode(null);
        }
    }

}