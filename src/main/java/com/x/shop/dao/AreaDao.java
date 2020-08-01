package com.x.shop.dao;

import com.x.shop.entity.Area;

import java.util.List;

/**
 * Dao - 地区
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface AreaDao extends BaseDao<Area, Long> {

    /**
     * 查找顶级地区
     *
     * @param count 数量
     * @return 顶级地区
     */
    List<Area> findRoots(Integer count);

}