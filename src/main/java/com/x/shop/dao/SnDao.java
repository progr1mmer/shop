package com.x.shop.dao;

import com.x.shop.entity.Sn;

/**
 * Dao - 序列号
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface SnDao {

    /**
     * 生成序列号
     *
     * @param type 类型
     * @return 序列号
     */
    String generate(Sn.Type type);

}