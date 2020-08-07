package com.x.shop.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Entity - 商品图片
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Embeddable
public class ProductImage implements Serializable, Comparable<ProductImage> {

    private static final long serialVersionUID = -673883300094536107L;

    /**
     * 标题
     */
    private String title;

    /**
     * 原图片
     */
    private String source;

    /**
     * 大图片
     */
    private String large;

    /**
     * 中图片
     */
    private String medium;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 文件
     */
    private MultipartFile file;

    private Boolean isB;

    @Length(max = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Min(0)
    @Column(name = "orders")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Transient
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Boolean getIsB() {
        return isB;
    }

    public void setIsB(Boolean isB) {
        this.isB = isB;
    }

    /**
     * 判断是否为空
     *
     * @return 是否为空
     */
    @Transient
    public boolean isEmpty() {
        return (getFile() == null || getFile().isEmpty()) && (StringUtils.isEmpty(getSource()) || StringUtils.isEmpty(getLarge()) || StringUtils.isEmpty(getMedium()) || StringUtils.isEmpty(getThumbnail()));
    }

    /**
     * 实现compareTo方法
     *
     * @param productImage 商品图片
     * @return 比较结果
     */
    @Override
    public int compareTo(ProductImage productImage) {
        return new CompareToBuilder().append(getOrder(), productImage.getOrder()).toComparison();
    }

}