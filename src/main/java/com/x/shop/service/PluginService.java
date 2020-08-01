package com.x.shop.service;

import com.x.shop.plugin.PaymentPlugin;
import com.x.shop.plugin.StoragePlugin;

import java.util.List;

/**
 * Service - 插件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface PluginService {

    /**
     * 获取支付插件
     *
     * @return 支付插件
     */
    List<PaymentPlugin> getPaymentPlugins();

    /**
     * 获取存储插件
     *
     * @return 存储插件
     */
    List<StoragePlugin> getStoragePlugins();

    /**
     * 获取支付插件
     *
     * @param isEnabled 是否启用
     * @return 支付插件
     */
    List<PaymentPlugin> getPaymentPlugins(boolean isEnabled);

    /**
     * 获取存储插件
     *
     * @param isEnabled 是否启用
     * @return 存储插件
     */
    List<StoragePlugin> getStoragePlugins(boolean isEnabled);

    /**
     * 获取支付插件
     *
     * @param id ID
     * @return 支付插件
     */
    PaymentPlugin getPaymentPlugin(String id);

    /**
     * 获取存储插件
     *
     * @param id ID
     * @return 存储插件
     */
    StoragePlugin getStoragePlugin(String id);

}