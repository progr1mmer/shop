package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.dao.BrandDao;
import com.x.shop.entity.Brand;
import com.x.shop.service.BrandService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 品牌
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("brandServiceImpl")
public class BrandServiceImpl extends BaseServiceImpl<Brand, Long> implements BrandService {

    @Resource(name = "brandDaoImpl")
    private BrandDao brandDao;

    @Override
    @Transactional(readOnly = true)
    @Cacheable("brand")
    public List<Brand> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return brandDao.findList(null, count, filters, orders);
    }

    @Override
    @Transactional
    @CacheEvict(value = "brand", allEntries = true)
    public void save(Brand brand) {
        super.save(brand);
    }

    @Override
    @Transactional
    @CacheEvict(value = "brand", allEntries = true)
    public Brand update(Brand brand) {
        return super.update(brand);
    }

    @Override
    @Transactional
    @CacheEvict(value = "brand", allEntries = true)
    public Brand update(Brand brand, String... ignoreProperties) {
        return super.update(brand, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = "brand", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "brand", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "brand", allEntries = true)
    public void delete(Brand brand) {
        super.delete(brand);
    }

}