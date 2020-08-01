package com.x.shop.dao.impl;

import com.x.shop.dao.AreaDao;
import com.x.shop.entity.Area;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Dao - 地区
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("areaDaoImpl")
public class AreaDaoImpl extends BaseDaoImpl<Area, Long> implements AreaDao {

    @Override
    public List<Area> findRoots(Integer count) {
        String jpql = "select area from Area area where area.parent is null order by area.order asc";
        TypedQuery<Area> query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

}