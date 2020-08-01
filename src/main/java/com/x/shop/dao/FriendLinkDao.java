package com.x.shop.dao;

import com.x.shop.entity.FriendLink;
import com.x.shop.entity.FriendLink.Type;

import java.util.List;

/**
 * Dao - 友情链接
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface FriendLinkDao extends BaseDao<FriendLink, Long> {

    /**
     * 查找友情链接
     *
     * @param type 类型
     * @return 友情链接
     */
    List<FriendLink> findList(Type type);

}