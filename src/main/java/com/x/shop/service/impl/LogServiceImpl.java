package com.x.shop.service.impl;

import com.x.shop.dao.LogDao;
import com.x.shop.entity.Log;
import com.x.shop.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 日志
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("logServiceImpl")
public class LogServiceImpl extends BaseServiceImpl<Log, Long> implements LogService {

    @Resource(name = "logDaoImpl")
    private LogDao logDao;

    @Override
    public void clear() {
        logDao.removeAll();
    }

}