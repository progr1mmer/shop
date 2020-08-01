package com.x.shop.service.impl;

import com.x.shop.common.Principal;
import com.x.shop.dao.AdminDao;
import com.x.shop.entity.Admin;
import com.x.shop.entity.Role;
import com.x.shop.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Service - 管理员
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

    @Resource(name = "adminDaoImpl")
    private AdminDao adminDao;

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return adminDao.usernameExists(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Admin findByUsername(String username) {
        return adminDao.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAuthorities(Long id) {
        List<String> authorities = new ArrayList<String>();
        Admin admin = adminDao.find(id);
        if (admin != null) {
            for (Role role : admin.getRoles()) {
                authorities.addAll(role.getAuthorities());
            }
        }
        return authorities;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            return subject.isAuthenticated();
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getCurrent() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return adminDao.find(principal.getId());
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public String getCurrentUsername() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal.getUsername();
            }
        }
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public void save(Admin admin) {
        super.save(admin);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public Admin update(Admin admin) {
        return super.update(admin);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public Admin update(Admin admin, String... ignoreProperties) {
        return super.update(admin, ignoreProperties);
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
    public void delete(Admin admin) {
        super.delete(admin);
    }

}