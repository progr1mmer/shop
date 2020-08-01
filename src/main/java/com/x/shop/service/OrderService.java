package com.x.shop.service;

import com.x.shop.common.Filter;
import com.x.shop.common.*;
import com.x.shop.common.Pageable;
import com.x.shop.entity.*;
import com.x.shop.entity.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Service - 订单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface OrderService extends BaseService<Order, Long> {

    /**
     * 根据订单编号查找订单
     *
     * @param sn 订单编号(忽略大小写)
     * @return 若不存在则返回null
     */
    Order findBySn(String sn);

    /**
     * 查找订单
     *
     * @param count   数量
     * @param filters 筛选
     * @param orders  排序
     * @return 订单
     */
    List<Order> findList(Integer count, List<Filter> filters, List<com.x.shop.common.Order> orders);

    /**
     * 查找订单分页
     *
     * @param orderStatus    订单状态
     * @param paymentStatus  支付状态
     * @param shippingStatus 配送状态
     * @param hasExpired     是否已过期
     * @param pageable       分页信息
     * @return 商品分页
     */
    Page<Order> findPage(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

    /**
     * 查询订单数量
     *
     * @param orderStatus    订单状态
     * @param paymentStatus  支付状态
     * @param shippingStatus 配送状态
     * @param hasExpired     是否已过期
     * @return 订单数量
     */
    Long count(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired);

    /**
     * 查询等待支付订单数量
     *
     * @return 等待支付订单数量
     */
    Long waitingPaymentCount();

    /**
     * 查询等待发货订单数量
     *
     * @return 等待发货订单数量
     */
    Long waitingShippingCount();

    /**
     * 获取销售额
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @return 销售额
     */
    BigDecimal getSalesAmount(Date beginDate, Date endDate);

    /**
     * 获取销售量
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @return 销售量
     */
    Integer getSalesVolume(Date beginDate, Date endDate);

    /**
     * 释放过期订单库存
     */
    void releaseStock();

    /**
     * 生成订单
     *
     * @param cart           购物车
     * @param consignee      收货人
     * @param areaId         地区
     * @param address        地址
     * @param zipCode        邮编
     * @param phone          手机
     * @param paymentMethod  支付方式
     * @param shippingMethod 配送方式
     * @param couponCode     优惠码
     * @param isInvoice      是否开据发票
     * @param invoiceTitle   发票抬头
     * @param useBalance     是否使用余额
     * @param memo           附言
     * @return 订单
     */
    Order build(Cart cart, String consignee, Long areaId, String address, String zipCode, String phone, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo);

    /**
     * 创建订单
     *
     * @param cart           购物车
     * @param consignee      收货人
     * @param areaId         地区
     * @param address        地址
     * @param zipCode        邮编
     * @param phone          手机
     * @param paymentMethod  支付方式
     * @param shippingMethod 配送方式
     * @param couponCode     优惠码
     * @param isInvoice      是否开据发票
     * @param invoiceTitle   发票抬头
     * @param useBalance     是否使用余额
     * @param memo           附言
     * @param operator       操作员
     * @return 订单
     */
    Order create(Cart cart, String consignee, Long areaId, String address, String zipCode, String phone, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo, Admin operator);

    /**
     * 更新订单
     *
     * @param order    订单
     * @param operator 操作员
     */
    void update(Order order, Admin operator);

    /**
     * 订单确认
     *
     * @param order    订单
     * @param operator 操作员
     */
    void confirm(Order order, Admin operator);

    /**
     * 订单完成
     *
     * @param order    订单
     * @param operator 操作员
     */
    void complete(Order order, Admin operator);

    /**
     * 订单取消
     *
     * @param order    订单
     * @param operator 操作员
     */
    void cancel(Order order, Admin operator);

    /**
     * 订单支付
     *
     * @param order    订单
     * @param payment  收款单
     * @param operator 操作员
     */
    void payment(Order order, Payment payment, Admin operator);

    /**
     * 订单退款
     *
     * @param order    订单
     * @param refunds  退款单
     * @param operator 操作员
     */
    void refunds(Order order, Refunds refunds, Admin operator);

    /**
     * 订单发货
     *
     * @param order    订单
     * @param shipping 发货单
     * @param operator 操作员
     */
    void shipping(Order order, Shipping shipping, Admin operator);

    /**
     * 订单退货
     *
     * @param order    订单
     * @param returns  退货单
     * @param operator 操作员
     */
    void returns(Order order, Returns returns, Admin operator);

}