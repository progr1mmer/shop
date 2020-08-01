package com.x.shop.dao.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.ReviewDao;
import com.x.shop.entity.Product;
import com.x.shop.entity.Review;
import com.x.shop.entity.Review.Type;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Dao - 评论
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("reviewDaoImpl")
public class ReviewDaoImpl extends BaseDaoImpl<Review, Long> implements ReviewDao {

    @Override
    public List<Review> findList(Product product, Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> criteriaQuery = criteriaBuilder.createQuery(Review.class);
        Root<Review> root = criteriaQuery.from(Review.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        /*if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }*/
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        }
        if (type == Type.positive) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("score"), 4));
        } else if (type == Type.moderate) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Number>get("score"), 3));
        } else if (type == Type.negative) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("score"), 2));
        }
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    @Override
    public Page<Review> findPage(Product product, Type type, Boolean isShow, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> criteriaQuery = criteriaBuilder.createQuery(Review.class);
        Root<Review> root = criteriaQuery.from(Review.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        /*if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }*/
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        }
        if (type == Type.positive) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("score"), 4));
        } else if (type == Type.moderate) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Number>get("score"), 3));
        } else if (type == Type.negative) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("score"), 2));
        }
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public Long count(Product product, Type type, Boolean isShow) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> criteriaQuery = criteriaBuilder.createQuery(Review.class);
        Root<Review> root = criteriaQuery.from(Review.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        /*if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }*/
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        }
        if (type == Type.positive) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("score"), 4));
        } else if (type == Type.moderate) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Number>get("score"), 3));
        } else if (type == Type.negative) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("score"), 2));
        }
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery, null);
    }

    @Override
    public boolean isReviewed(Product product) {
        if (product == null) {
            return false;
        }
        String jqpl = "select count(*) from Review review where review.member = :member and review.product = :product";
        //TODO
        //Long count = entityManager.createQuery(jqpl, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("product", product).getSingleResult();
        Long count = entityManager.createQuery(jqpl, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).getSingleResult();
        return count > 0;
    }

    @Override
    public long calculateTotalScore(Product product) {
        if (product == null) {
            return 0L;
        }
        String jpql = "select sum(review.score) from Review review where review.product = :product and review.isShow = :isShow";
        Long totalScore = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).setParameter("isShow", true).getSingleResult();
        return totalScore != null ? totalScore : 0L;
    }

    @Override
    public long calculateScoreCount(Product product) {
        if (product == null) {
            return 0L;
        }
        String jpql = "select count(*) from Review review where review.product = :product and review.isShow = :isShow";
        return entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).setParameter("isShow", true).getSingleResult();
    }

}