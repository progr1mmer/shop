package com.x.shop.dao.impl;

import com.x.shop.dao.TagDao;
import com.x.shop.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Dao - 标签
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("tagDaoImpl")
public class TagDaoImpl extends BaseDaoImpl<Tag, Long> implements TagDao {

    @Override
    public List<Tag> findList(Tag.Type type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root);
        if (type != null) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("type"), type));
        }
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")));
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
    }

}