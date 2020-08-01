package com.x.shop.dao;

import com.x.shop.entity.ProductCategory;

import java.util.List;

/**
 * Dao - 商品分类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ProductCategoryDao extends BaseDao<ProductCategory, Long> {

    /**
     * 查找顶级商品分类
     *
     * @param count 数量
     * @return 顶级商品分类
     */
    List<ProductCategory> findRoots(Integer count);

    /**
     * 查找上级商品分类
     *
     * @param productCategory 商品分类
     * @param count           数量
     * @return 上级商品分类
     */
    List<ProductCategory> findParents(ProductCategory productCategory, Integer count);

    /**
     * 查找下级商品分类
     *
     * @param productCategory 商品分类
     * @param count           数量
     * @return 下级商品分类
     */
    List<ProductCategory> findChildren(ProductCategory productCategory, Integer count);

}