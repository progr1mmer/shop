package com.x.shop.service;

import com.x.shop.common.LogConfig;

import java.util.List;

/**
 * Service - 日志配置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface LogConfigService {

    /**
     * 获取所有日志配置
     *
     * @return 所有日志配置
     */
    List<LogConfig> getAll();

}