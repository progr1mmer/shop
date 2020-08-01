package com.x.shop.common;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import java.math.BigDecimal;

/**
 * BigDecimal类型转换
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public class BigDecimalNumericFieldBridge implements TwoWayFieldBridge {

    /**
     * 获取BigDecimal对象
     *
     * @param name     名称
     * @param document document
     * @return BigDecimal对象
     */
    @Override
    public Object get(String name, Document document) {
        return new BigDecimal(document.getField(name).stringValue());
    }

    /**
     * 设置BigDecimal对象
     *
     * @param name          名称
     * @param value         值
     * @param document      document
     * @param luceneOptions luceneOptions
     */
    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        if (value != null) {
            BigDecimal decimalValue = (BigDecimal) value;
            luceneOptions.addNumericFieldToDocument(name, decimalValue.doubleValue(), document);
        }
    }

    @Override
    public String objectToString(Object object) {
        return object.toString();
    }

}