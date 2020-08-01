package com.x.shop.dao.impl;

import com.x.shop.dao.ParameterDao;
import com.x.shop.dao.ParameterGroupDao;
import com.x.shop.entity.Parameter;
import com.x.shop.entity.ParameterGroup;
import com.x.shop.entity.Product;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Dao - 参数组
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("parameterGroupDaoImpl")
public class ParameterGroupDaoImpl extends BaseDaoImpl<ParameterGroup, Long> implements ParameterGroupDao {

    @Resource(name = "parameterDaoImpl")
    private ParameterDao parameterDao;

    /**
     * 处理商品参数并更新
     *
     * @param parameterGroup 参数组
     * @return 参数组
     */
    @Override
    public ParameterGroup merge(ParameterGroup parameterGroup) {
        Assert.notNull(parameterGroup);

        Set<Parameter> excludes = new HashSet<>();
        CollectionUtils.select(parameterGroup.getParameters(), object -> {
            Parameter parameter = (Parameter) object;
            return parameter != null && parameter.getId() != null;
        }, excludes);
        List<Parameter> parameters = parameterDao.findList(parameterGroup, excludes);
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            String jpql = "select product from Product product join product.parameterValue parameterValue where index(parameterValue) = :parameter";
            List<Product> products = entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("parameter", parameter).getResultList();
            for (Product product : products) {
                product.getParameterValue().remove(parameter);
                if (i % 20 == 0) {
                    super.flush();
                    super.clear();
                }
            }
        }
        return super.merge(parameterGroup);
    }

    /**
     * 处理商品参数并删除
     *
     * @param parameterGroup 参数组
     * @return 参数组
     */
    @Override
    public void remove(ParameterGroup parameterGroup) {
        if (parameterGroup != null) {
            for (int i = 0; i < parameterGroup.getParameters().size(); i++) {
                Parameter parameter = parameterGroup.getParameters().get(i);
                String jpql = "select product from Product product join product.parameterValue parameterValue where index(parameterValue) = :parameter";
                List<Product> products = entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("parameter", parameter).getResultList();
                for (Product product : products) {
                    product.getParameterValue().remove(parameter);
                    if (i % 20 == 0) {
                        super.flush();
                        super.clear();
                    }
                }
            }
            super.remove(super.merge(parameterGroup));
        }
    }

}