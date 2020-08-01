package com.x.shop.service.impl;

import com.x.shop.dao.SeoDao;
import com.x.shop.entity.Seo;
import com.x.shop.service.SeoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - SEO设置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("seoServiceImpl")
public class SeoServiceImpl extends BaseServiceImpl<Seo, Long> implements SeoService {

    @Resource(name = "seoDaoImpl")
    private SeoDao seoDao;

    @Override
    @Transactional(readOnly = true)
    public Seo find(Seo.Type type) {
        return seoDao.find(type);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("seo")
    public Seo find(Seo.Type type, String cacheRegion) {
        return seoDao.find(type);
    }

    @Override
    @Transactional
    @CacheEvict(value = "seo", allEntries = true)
    public void save(Seo seo) {
        super.save(seo);
    }

    @Override
    @Transactional
    @CacheEvict(value = "seo", allEntries = true)
    public Seo update(Seo seo) {
        return super.update(seo);
    }

    @Override
    @Transactional
    @CacheEvict(value = "seo", allEntries = true)
    public Seo update(Seo seo, String... ignoreProperties) {
        return super.update(seo, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = "seo", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "seo", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "seo", allEntries = true)
    public void delete(Seo seo) {
        super.delete(seo);
    }

}