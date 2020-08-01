package com.x.shop.dao;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Article;
import com.x.shop.entity.ArticleCategory;
import com.x.shop.entity.Tag;

import java.util.Date;
import java.util.List;

/**
 * Dao - 文章
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ArticleDao extends BaseDao<Article, Long> {

    /**
     * 查找文章
     *
     * @param articleCategory 文章分类
     * @param tags            标签
     * @param count           数量
     * @param filters         筛选
     * @param orders          排序
     * @return 文章
     */
    List<Article> findList(ArticleCategory articleCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders);

    /**
     * 查找文章
     *
     * @param articleCategory 文章分类
     * @param beginDate       起始日期
     * @param endDate         结束日期
     * @param first           起始记录
     * @param count           数量
     * @return 文章
     */
    List<Article> findList(ArticleCategory articleCategory, Date beginDate, Date endDate, Integer first, Integer count);

    /**
     * 查找文章分页
     *
     * @param articleCategory 文章分类
     * @param tags            标签
     * @param pageable        分页信息
     * @return 文章分页
     */
    Page<Article> findPage(ArticleCategory articleCategory, List<Tag> tags, Pageable pageable);

}