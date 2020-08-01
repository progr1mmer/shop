package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.dao.PromotionDao;
import com.x.shop.entity.Promotion;
import com.x.shop.service.PromotionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 促销
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("promotionServiceImpl")
public class PromotionServiceImpl extends BaseServiceImpl<Promotion, Long> implements PromotionService {

    @Resource(name = "promotionDaoImpl")
    private PromotionDao promotionDao;

    @Override
    @Transactional(readOnly = true)
    public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders) {
        return promotionDao.findList(hasBegun, hasEnded, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("promotion")
    public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return promotionDao.findList(hasBegun, hasEnded, count, filters, orders);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"promotion", "product"}, allEntries = true)
    public void save(Promotion promotion) {
        super.save(promotion);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"promotion", "product"}, allEntries = true)
    public Promotion update(Promotion promotion) {
        return super.update(promotion);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"promotion", "product"}, allEntries = true)
    public Promotion update(Promotion promotion, String... ignoreProperties) {
        return super.update(promotion, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"promotion", "product"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"promotion", "product"}, allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"promotion", "product"}, allEntries = true)
    public void delete(Promotion promotion) {
        super.delete(promotion);
    }

}