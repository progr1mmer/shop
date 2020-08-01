package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.dao.TagDao;
import com.x.shop.entity.Tag;
import com.x.shop.entity.Tag.Type;
import com.x.shop.service.TagService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 标签
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("tagServiceImpl")
public class TagServiceImpl extends BaseServiceImpl<Tag, Long> implements TagService {

    @Resource(name = "tagDaoImpl")
    private TagDao tagDao;



    @Override
    @Transactional(readOnly = true)
    public List<Tag> findList(Type type) {
        return tagDao.findList(type);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("tag")
    public List<Tag> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return tagDao.findList(null, count, filters, orders);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tag", allEntries = true)
    public void save(Tag tag) {
        super.save(tag);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tag", allEntries = true)
    public Tag update(Tag tag) {
        return super.update(tag);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tag", allEntries = true)
    public Tag update(Tag tag, String... ignoreProperties) {
        return super.update(tag, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tag", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tag", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tag", allEntries = true)
    public void delete(Tag tag) {
        super.delete(tag);
    }

}