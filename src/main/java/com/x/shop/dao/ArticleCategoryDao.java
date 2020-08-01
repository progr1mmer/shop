package com.x.shop.dao;

import com.x.shop.entity.ArticleCategory;

import java.util.List;

/**
 * Dao - 文章分类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ArticleCategoryDao extends BaseDao<ArticleCategory, Long> {

    /**
     * 查找顶级文章分类
     *
     * @param count 数量
     * @return 顶级文章分类
     */
    List<ArticleCategory> findRoots(Integer count);

    /**
     * 查找上级文章分类
     *
     * @param articleCategory 文章分类
     * @param count           数量
     * @return 上级文章分类
     */
    List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count);

    /**
     * 查找下级文章分类
     *
     * @param articleCategory 文章分类
     * @param count           数量
     * @return 下级文章分类
     */
    List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count);

}