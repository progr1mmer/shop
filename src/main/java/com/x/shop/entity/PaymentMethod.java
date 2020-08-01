package com.x.shop.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 支付方式
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_payment_method")
public class PaymentMethod extends BaseOrderEntity {

    private static final long serialVersionUID = 6949816500116581199L;

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
     * 名称
     */
    private String name;

    /**
     * 方式
     */
    private Method method;

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * 图标
     */
    private String icon;

    /**
     * 介绍
     */
    private String description;

    /**
     * 内容
     */
    private String content;

    /**
     * 支持配送方式
     */
    private Set<ShippingMethod> shippingMethods = new HashSet<ShippingMethod>();

    /**
     * 订单
     */
    private Set<Order> orders = new HashSet<Order>();

    /**
     * 获取名称
     *
     * @return 名称
     */
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取方式
     *
     * @return 方式
     */
    @NotNull
    @Column(nullable = false)
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
     * 获取超时时间
     *
     * @return 超时时间
     */
    @Min(1)
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间
     */
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * 获取图标
     *
     * @return 图标
     */
    @Length(max = 200)
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取介绍
     *
     * @return 介绍
     */
    @Length(max = 200)
    public String getDescription() {
        return description;
    }

    /**
     * 设置介绍
     *
     * @param description 介绍
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取内容
     *
     * @return 内容
     */
    @Lob
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取支持配送方式
     *
     * @return 支持配送方式
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xx_payment_shipping_method",
            joinColumns = {@JoinColumn(name = "payment_method_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "shipping_method_id", referencedColumnName = "id")})
    @OrderBy("order asc")
    public Set<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }

    /**
     * 设置支持配送方式
     *
     * @param shippingMethods 支持配送方式
     */
    public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    /**
     * 获取订单
     *
     * @return 订单
     */
    @OneToMany(mappedBy = "paymentMethod", fetch = FetchType.LAZY)
    public Set<Order> getOrders() {
        return orders;
    }

    /**
     * 设置订单
     *
     * @param orders 订单
     */
    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    /**
     * 删除前处理
     */
    @PreRemove
    public void preRemove() {
        Set<Order> orders = getOrders();
        if (orders != null) {
            for (Order order : orders) {
                order.setPaymentMethod(null);
            }
        }
    }

}