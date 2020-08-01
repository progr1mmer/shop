package com.x.shop.service;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Product;
import com.x.shop.entity.Review;

import java.util.List;

/**
 * Service - 评论
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ReviewService extends BaseService<Review, Long> {

    /**
     * 查找评论
     *
     * @param product 商品
     * @param type    类型
     * @param isShow  是否显示
     * @param count   数量
     * @param filters 筛选
     * @param orders  排序
     * @return 评论
     */
    List<Review> findList(Product product, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

    /**
     * 查找评论(缓存)
     *
     * @param product     商品
     * @param type        类型
     * @param isShow      是否显示
     * @param count       数量
     * @param filters     筛选
     * @param orders      排序
     * @param cacheRegion 缓存区域
     * @return 评论(缓存)
     */
    List<Review> findList(Product product, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

    /**
     * 查找评论分页
     *
     * @param product  商品
     * @param type     类型
     * @param isShow   是否显示
     * @param pageable 分页信息
     * @return 评论分页
     */
    Page<Review> findPage(Product product, Review.Type type, Boolean isShow, Pageable pageable);

    /**
     * 查找评论数量
     *
     * @param product 商品
     * @param type    类型
     * @param isShow  是否显示
     * @return 评论数量
     */
    Long count(Product product, Review.Type type, Boolean isShow);

    /**
     * 判断会员是否已评论该商品
     *
     * @param product 商品
     * @return 是否已评论该商品
     */
    boolean isReviewed(Product product);

}