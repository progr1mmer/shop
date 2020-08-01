package com.x.shop.service.impl;

import com.x.shop.dao.PluginConfigDao;
import com.x.shop.entity.PluginConfig;
import com.x.shop.service.PluginConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service - 插件配置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("pluginConfigServiceImpl")
public class PluginConfigServiceImpl extends BaseServiceImpl<PluginConfig, Long> implements PluginConfigService {

    @Resource(name = "pluginConfigDaoImpl")
    private PluginConfigDao pluginConfigDao;

    @Override
    @Transactional(readOnly = true)
    public boolean pluginIdExists(String pluginId) {
        return pluginConfigDao.pluginIdExists(pluginId);
    }

    @Override
    @Transactional(readOnly = true)
    public PluginConfig findByPluginId(String pluginId) {
        return pluginConfigDao.findByPluginId(pluginId);
    }

}