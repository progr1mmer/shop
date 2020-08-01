package com.x.shop.service;

import com.x.shop.entity.ProductImage;

/**
 * Service - 商品图片
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface ProductImageService {

    /**
     * 生成商品图片
     *
     * @param productImage 商品图片
     */
    void build(ProductImage productImage);

}