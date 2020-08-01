package com.x.shop.dao;

import com.x.shop.entity.Parameter;
import com.x.shop.entity.ParameterGroup;

import java.util.List;
import java.util.Set;

/**
 * Dao - 参数
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ParameterDao extends BaseDao<Parameter, Long> {

    /**
     * 查找参数
     *
     * @param parameterGroup 参数组
     * @param excludes       排除参数
     * @return 参数
     */
    List<Parameter> findList(ParameterGroup parameterGroup, Set<Parameter> excludes);

}