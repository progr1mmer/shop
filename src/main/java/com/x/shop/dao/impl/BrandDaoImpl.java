package com.x.shop.dao.impl;

import com.x.shop.dao.BrandDao;
import com.x.shop.entity.Brand;
import org.springframework.stereotype.Repository;

/**
 * Dao - 品牌
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("brandDaoImpl")
public class BrandDaoImpl extends BaseDaoImpl<Brand, Long> implements BrandDao {

}