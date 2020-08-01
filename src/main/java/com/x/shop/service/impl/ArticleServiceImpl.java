package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.ArticleDao;
import com.x.shop.entity.Article;
import com.x.shop.entity.ArticleCategory;
import com.x.shop.entity.Tag;
import com.x.shop.service.ArticleService;
import com.x.shop.service.StaticService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Service - 文章
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("articleServiceImpl")
public class ArticleServiceImpl extends BaseServiceImpl<Article, Long> implements ArticleService, DisposableBean {

    /**
     * 查看点击数时间
     */
    private long viewHitsTime = System.currentTimeMillis();

    @Autowired
    private CacheManager cacheManager;
    @Resource(name = "articleDaoImpl")
    private ArticleDao articleDao;
    @Resource(name = "staticServiceImpl")
    private StaticService staticService;

    @Override
    @Transactional(readOnly = true)
    public List<Article> findList(ArticleCategory articleCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders) {
        return articleDao.findList(articleCategory, tags, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("article")
    public List<Article> findList(ArticleCategory articleCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return articleDao.findList(articleCategory, tags, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> findList(ArticleCategory articleCategory, Date beginDate, Date endDate, Integer first, Integer count) {
        return articleDao.findList(articleCategory, beginDate, endDate, first, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Article> findPage(ArticleCategory articleCategory, List<Tag> tags, Pageable pageable) {
        return articleDao.findPage(articleCategory, tags, pageable);
    }

    public long viewHits(Long id) {
        Ehcache cache = cacheManager.getEhcache(Article.HITS_CACHE_NAME);
        Element element = cache.get(id);
        Long hits;
        if (element != null) {
            hits = (Long) element.getObjectValue();
        } else {
            Article article = articleDao.find(id);
            if (article == null) {
                return 0L;
            }
            hits = article.getHits();
        }
        hits++;
        cache.put(new Element(id, hits));
        long time = System.currentTimeMillis();
        if (time > viewHitsTime + Article.HITS_CACHE_INTERVAL) {
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
        Ehcache cache = cacheManager.getEhcache(Article.HITS_CACHE_NAME);
        List<Long> ids = cache.getKeys();
        for (Long id : ids) {
            Article article = articleDao.find(id);
            if (article != null) {
                Element element = cache.get(id);
                long hits = (Long) element.getObjectValue();
                article.setHits(hits);
                articleDao.merge(article);
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"article", "articleCategory"}, allEntries = true)
    public void save(Article article) {
        Assert.notNull(article);

        super.save(article);
        articleDao.flush();
        staticService.build(article);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"article", "articleCategory"}, allEntries = true)
    public Article update(Article article) {
        Assert.notNull(article);

        Article pArticle = super.update(article);
        articleDao.flush();
        staticService.build(pArticle);
        return pArticle;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"article", "articleCategory"}, allEntries = true)
    public Article update(Article article, String... ignoreProperties) {
        return super.update(article, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"article", "articleCategory"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"article", "articleCategory"}, allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"article", "articleCategory"}, allEntries = true)
    public void delete(Article article) {
        if (article != null) {
            staticService.delete(article);
        }
        super.delete(article);
    }

}