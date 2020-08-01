package com.x.shop.dao.impl;

import com.x.shop.dao.LogDao;
import com.x.shop.entity.Log;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;

/**
 * Dao - 日志
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("logDaoImpl")
public class LogDaoImpl extends BaseDaoImpl<Log, Long> implements LogDao {

    @Override
    public void removeAll() {
        String jpql = "delete from Log log";
        entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).executeUpdate();
    }

}