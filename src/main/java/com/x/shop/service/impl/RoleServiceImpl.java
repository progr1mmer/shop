package com.x.shop.service.impl;

import com.x.shop.entity.Role;
import com.x.shop.service.RoleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 角色
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public void save(Role role) {
        super.save(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public Role update(Role role) {
        return super.update(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public Role update(Role role, String... ignoreProperties) {
        return super.update(role, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public void delete(Role role) {
        super.delete(role);
    }

}