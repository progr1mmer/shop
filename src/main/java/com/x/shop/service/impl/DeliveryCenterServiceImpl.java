package com.x.shop.service.impl;

import com.x.shop.dao.DeliveryCenterDao;
import com.x.shop.entity.DeliveryCenter;
import com.x.shop.service.DeliveryCenterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 广告
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("deliveryCenterServiceImpl")
public class DeliveryCenterServiceImpl extends BaseServiceImpl<DeliveryCenter, Long> implements DeliveryCenterService {

    @Resource(name = "deliveryCenterDaoImpl")
    private DeliveryCenterDao deliveryCenterDao;

    @Override
    @Transactional(readOnly = true)
    public DeliveryCenter findDefault() {
        return deliveryCenterDao.findDefault();
    }

}