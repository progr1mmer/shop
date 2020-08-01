package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.*;
import com.x.shop.common.Pageable;
import com.x.shop.common.Order;
import com.x.shop.dao.ProductDao;
import com.x.shop.entity.*;
import com.x.shop.service.ProductService;
import com.x.shop.service.StaticService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service - 商品
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("productServiceImpl")
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService, DisposableBean {

    /**
     * 查看点击数时间
     */
    private long viewHitsTime = System.currentTimeMillis();

    @Autowired
    private CacheManager cacheManager;
    @Resource(name = "productDaoImpl")
    private ProductDao productDao;
    @Resource(name = "staticServiceImpl")
    private StaticService staticService;

    @Override
    @Transactional(readOnly = true)
    public boolean snExists(String sn) {
        return productDao.snExists(sn);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findBySn(String sn) {
        return productDao.findBySn(sn);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean snUnique(String previousSn, String currentSn) {
        if (StringUtils.equalsIgnoreCase(previousSn, currentSn)) {
            return true;
        } else {
            if (productDao.snExists(currentSn)) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> search(String keyword, Boolean isGift, Integer count) {
        return productDao.search(keyword, isGift, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Product.OrderType orderType, Integer count, List<Filter> filters, List<com.x.shop.common.Order> orders) {
        return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("product")
    public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Product.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders,
                                  String cacheRegion) {
        return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count) {
        return productDao.findList(productCategory, beginDate, endDate, first, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count) {
        return productDao.findSalesList(beginDate, endDate, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Product.OrderType orderType, Pageable pageable) {
        return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long count(Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert) {
        return productDao.count(isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPurchased(Product product) {
        return productDao.isPurchased(product);
    }

    @Override
    public long viewHits(Long id) {
        Ehcache cache = cacheManager.getEhcache(Product.HITS_CACHE_NAME);
        Element element = cache.get(id);
        Long hits;
        if (element != null) {
            hits = (Long) element.getObjectValue();
        } else {
            Product product = productDao.find(id);
            if (product == null) {
                return 0L;
            }
            hits = product.getHits();
        }
        hits++;
        cache.put(new Element(id, hits));
        long time = System.currentTimeMillis();
        if (time > viewHitsTime + Product.HITS_CACHE_INTERVAL) {
            viewHitsTime = time;
            updateHits();
            cache.removeAll();
        }
        return hits;
    }

    @Override
    public void destroy() throws Exception {
        updateHits();
    }

    /**
     * 更新点击数
     */
    @SuppressWarnings("unchecked")
    private void updateHits() {
        Ehcache cache = cacheManager.getEhcache(Product.HITS_CACHE_NAME);
        List<Long> ids = cache.getKeys();
        for (Long id : ids) {
            Product product = productDao.find(id);
            if (product != null) {
                productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
                Element element = cache.get(id);
                long hits = (Long) element.getObjectValue();
                long increment = hits - product.getHits();
                Calendar nowCalendar = Calendar.getInstance();
                Calendar weekHitsCalendar = DateUtils.toCalendar(product.getWeekHitsDate());
                Calendar monthHitsCalendar = DateUtils.toCalendar(product.getMonthHitsDate());
                if (nowCalendar.get(Calendar.YEAR) != weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
                    product.setWeekHits(increment);
                } else {
                    product.setWeekHits(product.getWeekHits() + increment);
                }
                if (nowCalendar.get(Calendar.YEAR) != monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
                    product.setMonthHits(increment);
                } else {
                    product.setMonthHits(product.getMonthHits() + increment);
                }
                product.setHits(hits);
                product.setWeekHitsDate(new Date());
                product.setMonthHitsDate(new Date());
                productDao.merge(product);
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void save(Product product) {
        Assert.notNull(product);

        super.save(product);
        productDao.flush();
        staticService.build(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public Product update(Product product) {
        Assert.notNull(product);

        Product pProduct = super.update(product);
        productDao.flush();
        staticService.build(pProduct);
        return pProduct;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public Product update(Product product, String... ignoreProperties) {
        return super.update(product, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void delete(Product product) {
        if (product != null) {
            staticService.delete(product);
        }
        super.delete(product);
    }

}