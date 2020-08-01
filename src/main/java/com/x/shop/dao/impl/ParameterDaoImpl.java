package com.x.shop.dao.impl;

import com.x.shop.dao.ParameterDao;
import com.x.shop.entity.Parameter;
import com.x.shop.entity.ParameterGroup;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * Dao - 参数
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("parameterDaoImpl")
public class ParameterDaoImpl extends BaseDaoImpl<Parameter, Long> implements ParameterDao {

    @Override
    public List<Parameter> findList(ParameterGroup parameterGroup, Set<Parameter> excludes) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Parameter> criteriaQuery = criteriaBuilder.createQuery(Parameter.class);
        Root<Parameter> root = criteriaQuery.from(Parameter.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (parameterGroup != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parameterGroup"), parameterGroup));
        }
        if (excludes != null && !excludes.isEmpty()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(root.in(excludes)));
        }
        criteriaQuery.where(restrictions);
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
    }

}