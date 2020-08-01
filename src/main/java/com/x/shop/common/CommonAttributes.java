package com.x.shop.common;


/**
 * 公共参数
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public final class CommonAttributes {

    /**
     * 日期格式配比
     */
    public static final String[] DATE_PATTERNS = new String[]{"yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss"};

    /**
     * shop.xml文件路径
     */
    public static final String SHOP_XML_PATH = "/shop.xml";

    /**
     * 不可实例化
     */
    private CommonAttributes() {
    }

}