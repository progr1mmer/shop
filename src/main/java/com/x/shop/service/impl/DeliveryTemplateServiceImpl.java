package com.x.shop.service.impl;

import com.x.shop.dao.DeliveryTemplateDao;
import com.x.shop.entity.DeliveryTemplate;
import com.x.shop.service.DeliveryTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 快递单模板
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("deliveryTemplateServiceImpl")
public class DeliveryTemplateServiceImpl extends BaseServiceImpl<DeliveryTemplate, Long> implements DeliveryTemplateService {

    @Resource(name = "deliveryTemplateDaoImpl")
    private DeliveryTemplateDao deliveryTemplateDao;

    @Override
    @Transactional(readOnly = true)
    public DeliveryTemplate findDefault() {
        return deliveryTemplateDao.findDefault();
    }

}