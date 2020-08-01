package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.ProductDao;
import com.x.shop.dao.ReviewDao;
import com.x.shop.entity.Product;
import com.x.shop.entity.Review;
import com.x.shop.service.ReviewService;
import com.x.shop.service.StaticService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 评论
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("reviewServiceImpl")
public class ReviewServiceImpl extends BaseServiceImpl<Review, Long> implements ReviewService {

    @Resource(name = "reviewDaoImpl")
    private ReviewDao reviewDao;
    @Resource(name = "productDaoImpl")
    private ProductDao productDao;
    @Resource(name = "staticServiceImpl")
    private StaticService staticService;

    @Override
    @Transactional(readOnly = true)
    public List<Review> findList(Product product, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
        return reviewDao.findList(product, type, isShow, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("review")
    public List<Review> findList(Product product, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return reviewDao.findList(product, type, isShow, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Review> findPage(Product product, Review.Type type, Boolean isShow, Pageable pageable) {
        return reviewDao.findPage(product, type, isShow, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Long count(Product product, Review.Type type, Boolean isShow) {
        return reviewDao.count(product, type, isShow);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isReviewed(Product product) {
        return reviewDao.isReviewed(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void save(Review review) {
        super.save(review);
        Product product = review.getProduct();
        if (product != null) {
            reviewDao.flush();
            long totalScore = reviewDao.calculateTotalScore(product);
            long scoreCount = reviewDao.calculateScoreCount(product);
            product.setTotalScore(totalScore);
            product.setScoreCount(scoreCount);
            productDao.merge(product);
            reviewDao.flush();
            staticService.build(product);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public Review update(Review review) {
        Review pReview = super.update(review);
        Product product = pReview.getProduct();
        if (product != null) {
            reviewDao.flush();
            long totalScore = reviewDao.calculateTotalScore(product);
            long scoreCount = reviewDao.calculateScoreCount(product);
            product.setTotalScore(totalScore);
            product.setScoreCount(scoreCount);
            productDao.merge(product);
            reviewDao.flush();
            staticService.build(product);
        }
        return pReview;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public Review update(Review review, String... ignoreProperties) {
        return super.update(review, ignoreProperties);
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
    public void delete(Review review) {
        if (review != null) {
            super.delete(review);
            Product product = review.getProduct();
            if (product != null) {
                reviewDao.flush();
                long totalScore = reviewDao.calculateTotalScore(product);
                long scoreCount = reviewDao.calculateScoreCount(product);
                product.setTotalScore(totalScore);
                product.setScoreCount(scoreCount);
                productDao.merge(product);
                reviewDao.flush();
                staticService.build(product);
            }
        }
    }

}