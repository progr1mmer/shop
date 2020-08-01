package com.x.shop.dao;

import com.x.shop.entity.Log;

/**
 * Dao - 日志
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface LogDao extends BaseDao<Log, Long> {

    /**
     * 删除所有日志
     */
    void removeAll();

}