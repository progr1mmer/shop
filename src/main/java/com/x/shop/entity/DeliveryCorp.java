package com.x.shop.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 物流公司
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_delivery_corp")
public class DeliveryCorp extends BaseOrderEntity {

    private static final long serialVersionUID = 10595703086045998L;

    /**
     * 名称
     */
    private String name;

    /**
     * 网址
     */
    private String url;

    /**
     * 代码
     */
    private String code;

    /**
     * 配送方式
     */
    private Set<ShippingMethod> shippingMethods = new HashSet<ShippingMethod>();

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
     * 获取网址
     *
     * @return 网址
     */
    @Length(max = 200)
    public String getUrl() {
        return url;
    }

    /**
     * 设置网址
     *
     * @param url 网址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取代码
     *
     * @return 代码
     */
    @Length(max = 200)
    public String getCode() {
        return code;
    }

    /**
     * 设置代码
     *
     * @param code 代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取配送方式
     *
     * @return 配送方式
     */
    @OneToMany(mappedBy = "defaultDeliveryCorp", fetch = FetchType.LAZY)
    public Set<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }

    /**
     * 设置配送方式
     *
     * @param shippingMethods 配送方式
     */
    public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    /**
     * 删除前处理
     */
    @PreRemove
    public void preRemove() {
        Set<ShippingMethod> shippingMethods = getShippingMethods();
        if (shippingMethods != null) {
            for (ShippingMethod shippingMethod : shippingMethods) {
                shippingMethod.setDefaultDeliveryCorp(null);
            }
        }
    }

}