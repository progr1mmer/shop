package com.x.shop.service;

import com.x.shop.entity.ProductCategory;

import java.util.List;

/**
 * Service - 商品分类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ProductCategoryService extends BaseService<ProductCategory, Long> {

    /**
     * 查找顶级商品分类
     *
     * @return 顶级商品分类
     */
    List<ProductCategory> findRoots();

    /**
     * 查找顶级商品分类
     *
     * @param count 数量
     * @return 顶级商品分类
     */
    List<ProductCategory> findRoots(Integer count);

    /**
     * 查找顶级商品分类(缓存)
     *
     * @param count       数量
     * @param cacheRegion 缓存区域
     * @return 顶级商品分类(缓存)
     */
    List<ProductCategory> findRoots(Integer count, String cacheRegion);

    /**
     * 查找上级商品分类
     *
     * @param productCategory 商品分类
     * @return 上级商品分类
     */
    List<ProductCategory> findParents(ProductCategory productCategory);

    /**
     * 查找上级商品分类
     *
     * @param productCategory 商品分类
     * @param count           数量
     * @return 上级商品分类
     */
    List<ProductCategory> findParents(ProductCategory productCategory, Integer count);

    /**
     * 查找上级商品分类(缓存)
     *
     * @param productCategory 商品分类
     * @param count           数量
     * @param cacheRegion     缓存区域
     * @return 上级商品分类(缓存)
     */
    List<ProductCategory> findParents(ProductCategory productCategory, Integer count, String cacheRegion);

    /**
     * 查找商品分类树
     *
     * @return 商品分类树
     */
    List<ProductCategory> findTree();

    /**
     * 查找下级商品分类
     *
     * @param productCategory 商品分类
     * @return 下级商品分类
     */
    List<ProductCategory> findChildren(ProductCategory productCategory);

    /**
     * 查找下级商品分类
     *
     * @param productCategory 商品分类
     * @param count           数量
     * @return 下级商品分类
     */
    List<ProductCategory> findChildren(ProductCategory productCategory, Integer count);

    /**
     * 查找下级商品分类(缓存)
     *
     * @param productCategory 商品分类
     * @param count           数量
     * @param cacheRegion     缓存区域
     * @return 下级商品分类(缓存)
     */
    List<ProductCategory> findChildren(ProductCategory productCategory, Integer count, String cacheRegion);

}