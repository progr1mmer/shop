package com.x.shop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

/**
 * Entity - 排序基类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@MappedSuperclass
public abstract class BaseOrderEntity extends BaseEntity implements Comparable<BaseOrderEntity> {

    private static final long serialVersionUID = 5995013015967525827L;

    /**
     * "排序"属性名称
     */
    public static final String ORDER_PROPERTY_NAME = "order";

    /**
     * 排序
     */
    private Integer order;

    /**
     * 获取排序
     *
     * @return 排序
     */
    @JsonProperty
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @Min(0)
    @Column(name = "orders")
    public Integer getOrder() {
        return order;
    }

    /**
     * 设置排序
     *
     * @param order 排序
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * 实现compareTo方法
     *
     * @param orderEntity 排序对象
     * @return 比较结果
     */
    @Override
    public int compareTo(BaseOrderEntity orderEntity) {
        return new CompareToBuilder().append(getOrder(), orderEntity.getOrder()).append(getId(), orderEntity.getId()).toComparison();
    }

}