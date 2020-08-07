package com.x.shop.service.impl;

import com.x.shop.plugin.StoragePlugin;
import com.x.shop.service.PluginService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Service - 插件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("pluginServiceImpl")
public class PluginServiceImpl implements PluginService {

    @Resource
    private List<StoragePlugin> storagePlugins = new ArrayList<>();
    @Resource
    private Map<String, StoragePlugin> storagePluginMap = new HashMap<>();

    @Override
    public List<StoragePlugin> getStoragePlugins() {
        Collections.sort(storagePlugins);
        return storagePlugins;
    }

    @Override
    public List<StoragePlugin> getStoragePlugins(final boolean isEnabled) {
        List<StoragePlugin> result = new ArrayList<StoragePlugin>();
        CollectionUtils.select(storagePlugins, object -> {
            StoragePlugin storagePlugin = (StoragePlugin) object;
            return storagePlugin.getIsEnabled() == isEnabled;
        }, result);
        Collections.sort(result);
        return result;
    }

    @Override
    public StoragePlugin getStoragePlugin(String id) {
        return storagePluginMap.get(id);
    }

}