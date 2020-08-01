package com.x.shop.dao.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.ArticleDao;
import com.x.shop.entity.Article;
import com.x.shop.entity.ArticleCategory;
import com.x.shop.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * Dao - 文章
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("articleDaoImpl")
public class ArticleDaoImpl extends BaseDaoImpl<Article, Long> implements ArticleDao {

    @Override
    public List<Article> findList(ArticleCategory articleCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
        Root<Article> root = criteriaQuery.from(Article.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isPublication"), true));
        if (articleCategory != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("articleCategory"), articleCategory), criteriaBuilder.like(root.get("articleCategory").<String>get("treePath"), "%" + ArticleCategory.TREE_PATH_SEPARATOR + articleCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Article> subquery = criteriaQuery.subquery(Article.class);
            Root<Article> subqueryRoot = subquery.from(Article.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    @Override
    public List<Article> findList(ArticleCategory articleCategory, Date beginDate, Date endDate, Integer first, Integer count) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
        Root<Article> root = criteriaQuery.from(Article.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isPublication"), true));
        if (articleCategory != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("articleCategory"), articleCategory), criteriaBuilder.like(root.get("articleCategory").<String>get("treePath"), "%" + ArticleCategory.TREE_PATH_SEPARATOR + articleCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
        return super.findList(criteriaQuery, first, count, null, null);
    }

    @Override
    public Page<Article> findPage(ArticleCategory articleCategory, List<Tag> tags, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = criteriaBuilder.createQuery(Article.class);
        Root<Article> root = criteriaQuery.from(Article.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isPublication"), true));
        if (articleCategory != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("articleCategory"), articleCategory), criteriaBuilder.like(root.get("articleCategory").<String>get("treePath"), "%" + ArticleCategory.TREE_PATH_SEPARATOR + articleCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Article> subquery = criteriaQuery.subquery(Article.class);
            Root<Article> subqueryRoot = subquery.from(Article.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
        return super.findPage(criteriaQuery, pageable);
    }

}