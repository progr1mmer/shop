package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.dao.FriendLinkDao;
import com.x.shop.entity.FriendLink;
import com.x.shop.entity.FriendLink.Type;
import com.x.shop.service.FriendLinkService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 友情链接
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("friendLinkServiceImpl")
public class FriendLinkServiceImpl extends BaseServiceImpl<FriendLink, Long> implements FriendLinkService {

    @Resource(name = "friendLinkDaoImpl")
    public FriendLinkDao friendLinkDao;

    @Override
    @Transactional(readOnly = true)
    public List<FriendLink> findList(Type type) {
        return friendLinkDao.findList(type);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("friendLink")
    public List<FriendLink> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return friendLinkDao.findList(null, count, filters, orders);
    }

    @Override
    @Transactional
    @CacheEvict(value = "friendLink", allEntries = true)
    public void save(FriendLink friendLink) {
        super.save(friendLink);
    }

    @Override
    @Transactional
    @CacheEvict(value = "friendLink", allEntries = true)
    public FriendLink update(FriendLink friendLink) {
        return super.update(friendLink);
    }

    @Override
    @Transactional
    @CacheEvict(value = "friendLink", allEntries = true)
    public FriendLink update(FriendLink friendLink, String... ignoreProperties) {
        return super.update(friendLink, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = "friendLink", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "friendLink", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "friendLink", allEntries = true)
    public void delete(FriendLink friendLink) {
        super.delete(friendLink);
    }

}