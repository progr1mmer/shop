package com.x.shop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entity - 赠品项
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_gift_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"gift_id", "promotion_id"})})
public class GiftItem extends BaseEntity {

    private static final long serialVersionUID = 6593657730952481829L;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 赠品
     */
    private Product gift;

    /**
     * 促销
     */
    private Promotion promotion;

    /**
     * 获取数量
     *
     * @return 数量
     */
    @JsonProperty
    @NotNull
    @Min(1)
    @Column(nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取赠品
     *
     * @return 赠品
     */
    @JsonProperty
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public Product getGift() {
        return gift;
    }

    /**
     * 设置赠品
     *
     * @param gift 赠品
     */
    public void setGift(Product gift) {
        this.gift = gift;
    }

    /**
     * 获取促销
     *
     * @return 促销
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * 设置促销
     *
     * @param promotion 促销
     */
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

}