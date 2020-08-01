package com.x.shop.listener;

import com.x.shop.entity.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Listener - 创建日期、修改日期处理
 *
 * @author progr1mmer
 * @date Created on 2020/2/3
 */
public class EntityListener {

    /**
     * 保存前处理
     *
     * @param entity 基类
     */
    @PrePersist
    public void prePersist(BaseEntity entity) {
        entity.setCreateDate(new Date());
        entity.setModifyDate(new Date());
    }

    /**
     * 更新前处理
     *
     * @param entity 基类
     */
    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setModifyDate(new Date());
    }

}