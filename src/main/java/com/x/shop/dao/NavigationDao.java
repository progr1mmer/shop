package com.x.shop.dao;

import com.x.shop.entity.Navigation;

import java.util.List;

/**
 * Dao - 导航
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface NavigationDao extends BaseDao<Navigation, Long> {

    /**
     * 查找导航
     *
     * @param position 位置
     * @return 导航
     */
    List<Navigation> findList(Navigation.Position position);

}