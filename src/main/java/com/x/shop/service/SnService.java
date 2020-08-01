package com.x.shop.service;

import com.x.shop.entity.Sn;

/**
 * Service - 序列号
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface SnService {

    /**
     * 生成序列号
     *
     * @param type 类型
     * @return 序列号
     */
    String generate(Sn.Type type);

}