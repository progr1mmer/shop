package com.x.shop.service.impl;

import com.x.shop.plugin.PaymentPlugin;
import com.x.shop.plugin.StoragePlugin;
import com.x.shop.service.PluginService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
    private List<PaymentPlugin> paymentPlugins = new ArrayList<>();
    @Resource
    private List<StoragePlugin> storagePlugins = new ArrayList<>();
    @Resource
    private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<>();
    @Resource
    private Map<String, StoragePlugin> storagePluginMap = new HashMap<>();

    @Override
    public List<PaymentPlugin> getPaymentPlugins() {
        Collections.sort(paymentPlugins);
        return paymentPlugins;
    }

    @Override
    public List<StoragePlugin> getStoragePlugins() {
        Collections.sort(storagePlugins);
        return storagePlugins;
    }

    @Override
    public List<PaymentPlugin> getPaymentPlugins(final boolean isEnabled) {
        List<PaymentPlugin> result = new ArrayList<PaymentPlugin>();
        CollectionUtils.select(paymentPlugins, object -> {
            PaymentPlugin paymentPlugin = (PaymentPlugin) object;
            return paymentPlugin.getIsEnabled() == isEnabled;
        }, result);
        Collections.sort(result);
        return result;
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
    public PaymentPlugin getPaymentPlugin(String id) {
        return paymentPluginMap.get(id);
    }

    @Override
    public StoragePlugin getStoragePlugin(String id) {
        return storagePluginMap.get(id);
    }

}