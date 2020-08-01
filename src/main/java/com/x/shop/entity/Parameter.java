package com.x.shop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Entity - 参数
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_parameter")
public class Parameter extends BaseOrderEntity {

    private static final long serialVersionUID = -5833568086582136314L;

    /**
     * 名称
     */
    private String name;

    /**
     * 参数组
     */
    private ParameterGroup parameterGroup;

    /**
     * 获取名称
     *
     * @return 名称
     */
    @JsonProperty
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
     * 获取参数组
     *
     * @return 参数组
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    public ParameterGroup getParameterGroup() {
        return parameterGroup;
    }

    /**
     * 设置参数组
     *
     * @param parameterGroup 参数组
     */
    public void setParameterGroup(ParameterGroup parameterGroup) {
        this.parameterGroup = parameterGroup;
    }

}