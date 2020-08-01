package com.x.shop.dao.impl;

import com.x.shop.dao.FriendLinkDao;
import com.x.shop.entity.FriendLink;
import com.x.shop.entity.FriendLink.Type;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Dao - 友情链接
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("friendLinkDaoImpl")
public class FriendLinkDaoImpl extends BaseDaoImpl<FriendLink, Long> implements FriendLinkDao {

    @Override
    public List<FriendLink> findList(Type type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FriendLink> criteriaQuery = criteriaBuilder.createQuery(FriendLink.class);
        Root<FriendLink> root = criteriaQuery.from(FriendLink.class);
        criteriaQuery.select(root);
        if (type != null) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("type"), type));
        }
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")));
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
    }

}