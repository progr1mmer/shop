package com.x.shop.service.impl;

import com.x.shop.common.CommonAttributes;
import com.x.shop.common.LogConfig;
import com.x.shop.service.LogConfigService;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Service - 日志配置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("logConfigServiceImpl")
public class LogConfigServiceImpl implements LogConfigService {

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable("logConfig")
    public List<LogConfig> getAll() {
        try {
            File shopXmlFile = new ClassPathResource(CommonAttributes.SHOP_XML_PATH).getFile();
            Document document = new SAXReader().read(shopXmlFile);
            List<org.dom4j.Element> elements = document.selectNodes("/shop/logConfig");
            List<LogConfig> logConfigs = new ArrayList<LogConfig>();
            for (org.dom4j.Element element : elements) {
                String operation = element.attributeValue("operation");
                String urlPattern = element.attributeValue("urlPattern");
                LogConfig logConfig = new LogConfig();
                logConfig.setOperation(operation);
                logConfig.setUrlPattern(urlPattern);
                logConfigs.add(logConfig);
            }
            return logConfigs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}