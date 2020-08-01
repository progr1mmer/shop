package com.x.shop.util;

import org.springframework.beans.BeanUtils;

/**
 * @author progr1mmer
 * @date Created on 2020/3/15
 */
public class EntityUtils {

    /*public static <D extends BaseEntity, V extends BaseVo> V convert(D entity, V vo) {
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }*/

    public static <T> T convert(Object obj, T t) {
        BeanUtils.copyProperties(obj, t);
        return t;
    }

}
