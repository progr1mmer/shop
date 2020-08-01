package com.x.shop.service.impl;

import com.x.shop.dao.SnDao;
import com.x.shop.entity.Sn.Type;
import com.x.shop.service.SnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 序列号
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("snServiceImpl")
public class SnServiceImpl implements SnService {

    @Resource(name = "snDaoImpl")
    private SnDao snDao;

    @Override
    @Transactional
    public String generate(Type type) {
        return snDao.generate(type);
    }

}