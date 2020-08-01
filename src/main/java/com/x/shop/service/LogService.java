package com.x.shop.service;

import com.x.shop.entity.Log;

/**
 * Service - 日志
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface LogService extends BaseService<Log, Long> {

    /**
     * 清空日志
     */
    void clear();

}