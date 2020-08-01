package com.x.shop.service.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.BaseDao;
import com.x.shop.entity.BaseEntity;
import com.x.shop.service.BaseService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Service - 基类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Transactional
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    /**
     * 更新忽略属性
     */
    private static final String[] UPDATE_IGNORE_PROPERTIES = new String[]{
            BaseEntity.ID_PROPERTY_NAME,
            BaseEntity.CREATE_DATE_PROPERTY_NAME,
            BaseEntity.MODIFY_DATE_PROPERTY_NAME
    };

    /**
     * baseDao
     */
    @Autowired
    private BaseDao<T, ID> baseDao;

    @Override
    @Transactional(readOnly = true)
    public T find(ID id) {
        return baseDao.find(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return findList(null, null, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * TODO @SafeVarargs
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findList(ID... ids) {
        List<T> result = new ArrayList<>();
        if (ids != null) {
            for (ID id : ids) {
                T entity = find(id);
                if (entity != null) {
                    result.add(entity);
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Integer count, String filters, Sort sort) {
        filters = HtmlUtils.htmlUnescape(filters);
        return findList(null, count, filters, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Integer count, List<Filter> filters, List<Order> orders) {
        return findList(null, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Integer first, Integer count, String filters, Sort sort) {
        if (filters != null) {
            filters = HtmlUtils.htmlUnescape(filters);
        }
        return baseDao.findList(first, count, filters, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
        return baseDao.findList(first, count, filters, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findPage(Pageable pageable) {
        return baseDao.findPage(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return count(new Filter[]{});
    }

    @Override
    @Transactional(readOnly = true)
    public long count(Filter... filters) {
        return baseDao.count(filters);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return baseDao.find(id) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Filter... filters) {
        return baseDao.count(filters) > 0;
    }

    @Override
    @Transactional
    public void save(T entity) {
        baseDao.persist(entity);
    }

    @Override
    @Transactional
    public T update(T entity) {
        return baseDao.merge(entity);
    }

    @Override
    @Transactional
    public T update(T entity, String... ignoreProperties) {
        Assert.notNull(entity);
        if (baseDao.isManaged(entity)) {
            throw new IllegalArgumentException("Entity must not be managed");
        }
        T persistant = baseDao.find(baseDao.getIdentifier(entity));
        if (persistant != null) {
            copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
            return update(persistant);
        } else {
            return update(entity);
        }
    }

    @Override
    @Transactional
    public void delete(ID id) {
        delete(baseDao.find(id));
    }

    @Override
    @Transactional
    public void delete(ID... ids) {
        if (ids != null) {
            for (ID id : ids) {
                delete(baseDao.find(id));
            }
        }
    }

    @Override
    @Transactional
    public void delete(T entity) {
        baseDao.remove(entity);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void copyProperties(Object source, Object target, String[] ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null && (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object sourceValue = readMethod.invoke(source);
                        Object targetValue = readMethod.invoke(target);
                        if (sourceValue != null && targetValue != null && targetValue instanceof Collection) {
                            Collection collection = (Collection) targetValue;
                            collection.clear();
                            collection.addAll((Collection) sourceValue);
                        } else {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, sourceValue);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

}