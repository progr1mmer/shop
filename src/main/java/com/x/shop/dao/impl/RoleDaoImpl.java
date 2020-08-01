package com.x.shop.dao.impl;

import com.x.shop.dao.RoleDao;
import com.x.shop.entity.Role;
import org.springframework.stereotype.Repository;

/**
 * Dao - 角色
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {

}