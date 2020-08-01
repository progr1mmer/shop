package com.x.shop.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Entity - 评论
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_review")
public class Review extends BaseEntity {

    private static final long serialVersionUID = 8795901519290584100L;

    /**
     * 访问路径前缀
     */
    private static final String PATH_PREFIX = "/review/content";

    /**
     * 访问路径后缀
     */
    private static final String PATH_SUFFIX = ".html";

    /**
     * 类型
     */
    public enum Type {

        /**
         * 好评
         */
        positive,

        /**
         * 中评
         */
        moderate,

        /**
         * 差评
         */
        negative
    }

    /**
     * 评分
     */
    private Integer score;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * IP
     */
    private String ip;

    /**
     * 商品
     */
    private Product product;

    /**
     * 获取评分
     *
     * @return 评分
     */
    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false, updatable = false)
    public Integer getScore() {
        return score;
    }

    /**
     * 设置评分
     *
     * @param score 评分
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 获取内容
     *
     * @return 内容
     */
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false, updatable = false)
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
     * 获取是否显示
     *
     * @return 是否显示
     */
    @Column(nullable = false)
    public Boolean getIsShow() {
        return isShow;
    }

    /**
     * 设置是否显示
     *
     * @param isShow 是否显示
     */
    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    /**
     * 获取IP
     *
     * @return IP
     */
    @Column(nullable = false, updatable = false)
    public String getIp() {
        return ip;
    }

    /**
     * 设置IP
     *
     * @param ip IP
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取商品
     *
     * @return 商品
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    /**
     * 设置商品
     *
     * @param product 商品
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * 获取访问路径
     *
     * @return 访问路径
     */
    @Transient
    public String getPath() {
        if (getProduct() != null && getProduct().getId() != null) {
            return PATH_PREFIX + "/" + getProduct().getId() + PATH_SUFFIX;
        }
        return null;
    }

}