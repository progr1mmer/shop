package com.x.shop.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity - 退款单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_refunds")
public class Refunds extends BaseEntity {

    private static final long serialVersionUID = 354885216604823632L;

    /**
     * 方式
     */
    public enum Method {
        /**
         * 线下支付
         */
        offline
    }

    /**
     * 编号
     */
    private String sn;

    /**
     * 方式
     */
    private Method method;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 退款银行
     */
    private String bank;

    /**
     * 退款账号
     */
    private String account;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 收款人
     */
    private String payee;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 备注
     */
    private String memo;

    /**
     * 订单
     */
    private Order order;

    /**
     * 获取编号
     *
     * @return 编号
     */
    @Column(nullable = false, updatable = false, unique = true, length = 100)
    public String getSn() {
        return sn;
    }

    /**
     * 设置编号
     *
     * @param sn 编号
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 获取方式
     *
     * @return 方式
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    public Method getMethod() {
        return method;
    }

    /**
     * 设置方式
     *
     * @param method 方式
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * 获取支付方式
     *
     * @return 支付方式
     */
    @Column(updatable = false)
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * 设置支付方式
     *
     * @param paymentMethod 支付方式
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * 获取退款银行
     *
     * @return 退款银行
     */
    @Length(max = 200)
    @Column(updatable = false)
    public String getBank() {
        return bank;
    }

    /**
     * 设置退款银行
     *
     * @param bank 退款银行
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 获取退款账号
     *
     * @return 退款账号
     */
    @Length(max = 200)
    @Column(updatable = false)
    public String getAccount() {
        return account;
    }

    /**
     * 设置退款账号
     *
     * @param account 退款账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取退款金额
     *
     * @return 退款金额
     */
    @NotNull
    @Min(0)
    @Digits(integer = 21, fraction = 2)
    @Column(nullable = false, updatable = false, precision = 21, scale = 2)
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置退款金额
     *
     * @param amount 退款金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取收款人
     *
     * @return 收款人
     */
    @Length(max = 200)
    @Column(updatable = false)
    public String getPayee() {
        return payee;
    }

    /**
     * 设置收款人
     *
     * @param payee 收款人
     */
    public void setPayee(String payee) {
        this.payee = payee;
    }

    /**
     * 获取操作员
     *
     * @return 操作员
     */
    @Column(nullable = false, updatable = false)
    public String getOperator() {
        return operator;
    }

    /**
     * 设置操作员
     *
     * @param operator 操作员
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取备注
     *
     * @return 备注
     */
    @Length(max = 200)
    @Column(updatable = false)
    public String getMemo() {
        return memo;
    }

    /**
     * 设置备注
     *
     * @param memo 备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取订单
     *
     * @return 订单
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
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

}