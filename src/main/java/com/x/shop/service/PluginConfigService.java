package com.x.shop.service;

import com.x.shop.entity.PluginConfig;

/**
 * Service - 插件配置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface PluginConfigService extends BaseService<PluginConfig, Long> {

    /**
     * 判断插件ID是否存在
     *
     * @param pluginId 插件ID
     * @return 插件ID是否存在
     */
    boolean pluginIdExists(String pluginId);

    /**
     * 根据插件ID查找插件配置
     *
     * @param pluginId 插件ID
     * @return 插件配置，若不存在则返回null
     */
    PluginConfig findByPluginId(String pluginId);

}