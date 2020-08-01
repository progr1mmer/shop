package com.x.shop.dao;

import com.x.shop.entity.Admin;

/**
 * Dao - 管理员
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface AdminDao extends BaseDao<Admin, Long> {

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名(忽略大小写)
     * @return 用户名是否存在
     */
    boolean usernameExists(String username);

    /**
     * 根据用户名查找管理员
     *
     * @param username 用户名(忽略大小写)
     * @return 管理员，若不存在则返回null
     */
    Admin findByUsername(String username);

}