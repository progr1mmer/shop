package com.x.shop.dao.impl;

import com.x.shop.dao.AdminDao;
import com.x.shop.entity.Admin;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

/**
 * Dao - 管理员
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("adminDaoImpl")
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {

    @Override
    public boolean usernameExists(String username) {
        if (username == null) {
            return false;
        }
        String jpql = "select count(*) from Admin admin where lower(admin.username) = lower(:username)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
        return count > 0;
    }

    @Override
    public Admin findByUsername(String username) {
        if (username == null) {
            return null;
        }
        try {
            String jpql = "select admin from Admin admin where lower(admin.username) = lower(:username)";
            return entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}