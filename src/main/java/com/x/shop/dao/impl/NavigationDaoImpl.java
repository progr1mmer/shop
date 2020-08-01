package com.x.shop.dao.impl;

import com.x.shop.dao.NavigationDao;
import com.x.shop.entity.Navigation;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Dao - 导航
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("navigationDaoImpl")
public class NavigationDaoImpl extends BaseDaoImpl<Navigation, Long> implements NavigationDao {

    @Override
    public List<Navigation> findList(Navigation.Position position) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Navigation> criteriaQuery = criteriaBuilder.createQuery(Navigation.class);
        Root<Navigation> root = criteriaQuery.from(Navigation.class);
        criteriaQuery.select(root);
        if (position != null) {
            criteriaQuery.where(criteriaBuilder.equal(root.get("position"), position));
        }
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")));
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
    }

}