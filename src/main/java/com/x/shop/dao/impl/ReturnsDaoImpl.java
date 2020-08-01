package com.x.shop.dao.impl;

import com.x.shop.dao.ReturnsDao;
import com.x.shop.entity.Returns;
import org.springframework.stereotype.Repository;

/**
 * Dao - 退货单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("returnsDaoImpl")
public class ReturnsDaoImpl extends BaseDaoImpl<Returns, Long> implements ReturnsDao {

}