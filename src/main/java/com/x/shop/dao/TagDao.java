package com.x.shop.dao;

import com.x.shop.entity.Tag;

import java.util.List;

/**
 * Dao - 标签
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface TagDao extends BaseDao<Tag, Long> {

    /**
     * 查找标签
     *
     * @param type 类型
     * @return 标签
     */
    List<Tag> findList(Tag.Type type);

}