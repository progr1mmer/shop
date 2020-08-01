package com.x.shop.dao.impl;

import com.x.shop.dao.SeoDao;
import com.x.shop.entity.Seo;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

/**
 * Dao - SEO设置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("seoDaoImpl")
public class SeoDaoImpl extends BaseDaoImpl<Seo, Long> implements SeoDao {

    @Override
    public Seo find(Seo.Type type) {
        if (type == null) {
            return null;
        }
        try {
            String jpql = "select seo from Seo seo where seo.type = :type";
            return entityManager.createQuery(jpql, Seo.class).setFlushMode(FlushModeType.COMMIT).setParameter("type", type).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}