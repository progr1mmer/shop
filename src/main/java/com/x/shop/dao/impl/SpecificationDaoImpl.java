package com.x.shop.dao.impl;

import com.x.shop.dao.SpecificationDao;
import com.x.shop.entity.Specification;
import org.springframework.stereotype.Repository;

/**
 * Dao - 规格
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("specificationDaoImpl")
public class SpecificationDaoImpl extends BaseDaoImpl<Specification, Long> implements SpecificationDao {

}