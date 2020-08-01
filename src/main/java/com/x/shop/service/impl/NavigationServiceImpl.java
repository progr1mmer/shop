package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.dao.NavigationDao;
import com.x.shop.entity.Navigation;
import com.x.shop.service.NavigationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 导航
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("navigationServiceImpl")
public class NavigationServiceImpl extends BaseServiceImpl<Navigation, Long> implements NavigationService {

    @Resource(name = "navigationDaoImpl")
    private NavigationDao navigationDao;

    @Override
    @Transactional(readOnly = true)
    public List<Navigation> findList(Navigation.Position position) {
        return navigationDao.findList(position);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("navigation")
    public List<Navigation> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return navigationDao.findList(null, count, filters, orders);
    }

    @Override
    @Transactional
    @CacheEvict(value = "navigation", allEntries = true)
    public void save(Navigation navigation) {
        super.save(navigation);
    }

    @Override
    @Transactional
    @CacheEvict(value = "navigation", allEntries = true)
    public Navigation update(Navigation navigation) {
        return super.update(navigation);
    }

    @Override
    @Transactional
    @CacheEvict(value = "navigation", allEntries = true)
    public Navigation update(Navigation navigation, String... ignoreProperties) {
        return super.update(navigation, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = "navigation", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "navigation", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "navigation", allEntries = true)
    public void delete(Navigation navigation) {
        super.delete(navigation);
    }

}