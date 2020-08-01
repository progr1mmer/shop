package com.x.shop.dao.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.BaseDao;
import com.x.shop.entity.BaseOrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Dao - 基类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public abstract class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

    private static final Pattern DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
    private static final Pattern UTC_DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z");

    /**
     * 实体类类型
     */
    private Class<T> entityClass;

    /**
     * 别名数
     */
    private static volatile long aliasCount = 0;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        Type type = getClass().getGenericSuperclass();
        Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
        entityClass = (Class<T>) parameterizedType[0];
    }

    @Override
    public T find(ID id) {
        if (id != null) {
            return entityManager.find(entityClass, id);
        }
        return null;
    }

    @Override
    public T find(ID id, LockModeType lockModeType) {
        if (id != null) {
            if (lockModeType != null) {
                return entityManager.find(entityClass, id, lockModeType);
            } else {
                return entityManager.find(entityClass, id);
            }
        }
        return null;
    }

    @Override
    public List<T> findList(Integer first, Integer count, String filters, Sort sort) {
        return findList(first, count, parseFiler(filters), parserOrder(sort));
    }

    @Override
    public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return findList(criteriaQuery, first, count, filters, orders);
    }

    @Override
    public Page<T> findPage(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return findPage(criteriaQuery, pageable);
    }

    @Override
    public long count(Filter... filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return count(criteriaQuery, filters != null ? Arrays.asList(filters) : null);
    }

    @Override
    public long count(String filters) {
        List<Filter> filterList = parseFiler(filters);
        return count(filterList.toArray(new Filter[filterList.size()]));
    }

    @Override
    public void persist(T entity) {
        Assert.notNull(entity);
        entityManager.persist(entity);
    }

    @Override
    public T merge(T entity) {
        Assert.notNull(entity);
        return entityManager.merge(entity);
    }

    @Override
    public void remove(T entity) {
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public void refresh(T entity) {
        if (entity != null) {
            entityManager.refresh(entity);
        }
    }

    @Override
    public void refresh(T entity, LockModeType lockModeType) {
        if (entity != null) {
            if (lockModeType != null) {
                entityManager.refresh(entity, lockModeType);
            } else {
                entityManager.refresh(entity);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ID getIdentifier(T entity) {
        Assert.notNull(entity);
        return (ID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }

    @Override
    public boolean isManaged(T entity) {
        return entityManager.contains(entity);
    }

    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }

    @Override
    public void lock(T entity, LockModeType lockModeType) {
        if (entity != null && lockModeType != null) {
            entityManager.lock(entity, lockModeType);
        }
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    protected List<T> findList(CriteriaQuery<T> criteriaQuery, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Root<T> root = getRoot(criteriaQuery);
        addRestrictions(criteriaQuery, filters);
        addOrders(criteriaQuery, orders);
        if (criteriaQuery.getOrderList().isEmpty()) {
            if (BaseOrderEntity.class.isAssignableFrom(entityClass)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(BaseOrderEntity.ORDER_PROPERTY_NAME)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(BaseOrderEntity.CREATE_DATE_PROPERTY_NAME)));
            }
        }
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        if (first != null) {
            query.setFirstResult(first);
        }
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

    protected Page<T> findPage(CriteriaQuery<T> criteriaQuery, Pageable pageable) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());

        if (pageable == null) {
            pageable = new Pageable();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Root<T> root = getRoot(criteriaQuery);
        addRestrictions(criteriaQuery, pageable);
        addOrders(criteriaQuery, pageable);
        if (criteriaQuery.getOrderList().isEmpty()) {
            if (BaseOrderEntity.class.isAssignableFrom(entityClass)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(BaseOrderEntity.ORDER_PROPERTY_NAME)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(BaseOrderEntity.CREATE_DATE_PROPERTY_NAME)));
            }
        }
        long total = count(criteriaQuery, null);
        int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
        if (totalPages < pageable.getPageNumber()) {
            pageable.setPageNumber(totalPages);
        }
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new Page<T>(query.getResultList(), total, pageable);
    }

    protected Long count(CriteriaQuery<T> criteriaQuery, List<Filter> filters) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        addRestrictions(criteriaQuery, filters);

        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        for (Root<?> root : criteriaQuery.getRoots()) {
            Root<?> dest = countCriteriaQuery.from(root.getJavaType());
            dest.alias(getAlias(root));
            copyJoins(root, dest);
        }

        Root<?> countRoot = getRoot(countCriteriaQuery, criteriaQuery.getResultType());
        countCriteriaQuery.select(criteriaBuilder.count(countRoot));

        if (criteriaQuery.getGroupList() != null) {
            countCriteriaQuery.groupBy(criteriaQuery.getGroupList());
        }
        if (criteriaQuery.getGroupRestriction() != null) {
            countCriteriaQuery.having(criteriaQuery.getGroupRestriction());
        }
        if (criteriaQuery.getRestriction() != null) {
            countCriteriaQuery.where(criteriaQuery.getRestriction());
        }
        return entityManager.createQuery(countCriteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
    }

    private synchronized String getAlias(Selection<?> selection) {
        if (selection != null) {
            String alias = selection.getAlias();
            if (alias == null) {
                if (aliasCount >= 1000) {
                    aliasCount = 0;
                }
                alias = "alias" + aliasCount++;
                selection.alias(alias);
            }
            return alias;
        }
        return null;
    }

    private Root<T> getRoot(CriteriaQuery<T> criteriaQuery) {
        if (criteriaQuery != null) {
            return getRoot(criteriaQuery, criteriaQuery.getResultType());
        }
        return null;
    }

    private Root<T> getRoot(CriteriaQuery<?> criteriaQuery, Class<T> clazz) {
        if (criteriaQuery != null && criteriaQuery.getRoots() != null && clazz != null) {
            for (Root<?> root : criteriaQuery.getRoots()) {
                if (clazz.equals(root.getJavaType())) {
                    return (Root<T>) root.as(clazz);
                }
            }
        }
        return null;
    }

    private void copyJoins(From<?, ?> from, From<?, ?> to) {
        for (Join<?, ?> join : from.getJoins()) {
            Join<?, ?> toJoin = to.join(join.getAttribute().getName(), join.getJoinType());
            toJoin.alias(getAlias(join));
            copyJoins(join, toJoin);
        }
        for (Fetch<?, ?> fetch : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(fetch.getAttribute().getName());
            copyFetches(fetch, toFetch);
        }
    }

    private void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to) {
        for (Fetch<?, ?> fetch : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(fetch.getAttribute().getName());
            copyFetches(fetch, toFetch);
        }
    }

    private void addRestrictions(CriteriaQuery<T> criteriaQuery, List<Filter> filters) {
        if (criteriaQuery == null || filters == null || filters.isEmpty()) {
            return;
        }
        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate restrictions = criteriaQuery.getRestriction() != null ? criteriaQuery.getRestriction() : criteriaBuilder.conjunction();
        for (Filter filter : filters) {
            if (filter == null || StringUtils.isEmpty(filter.getProperty())) {
                continue;
            }
            if (filter.getOperator() == Filter.Operator.eq && filter.getValue() != null) {
                if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                } else {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue()));
                }
            } else if (filter.getOperator() == Filter.Operator.ne && filter.getValue() != null) {
                if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(criteriaBuilder.lower(root.<String>get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                } else {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue()));
                }
            } else if (filter.getOperator() == Filter.Operator.gt && filter.getValue() != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(root.<Number>get(filter.getProperty()), (Number) filter.getValue()));
            } else if (filter.getOperator() == Filter.Operator.lt && filter.getValue() != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt(root.<Number>get(filter.getProperty()), (Number) filter.getValue()));
            } else if (filter.getOperator() == Filter.Operator.ge && filter.getValue() != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get(filter.getProperty()), (Number) filter.getValue()));
            } else if (filter.getOperator() == Filter.Operator.le && filter.getValue() != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get(filter.getProperty()), (Number) filter.getValue()));
            } else if (filter.getOperator() == Filter.Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get(filter.getProperty()), (String) filter.getValue()));
            } else if (filter.getOperator() == Filter.Operator.in && filter.getValue() != null) {
                restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).in((Object[])filter.getValue()));
            } else if (filter.getOperator() == Filter.Operator.isNull) {
                restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).isNull());
            } else if (filter.getOperator() == Filter.Operator.isNotNull) {
                restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).isNotNull());
            }
        }
        criteriaQuery.where(restrictions);
    }

    private void addRestrictions(CriteriaQuery<T> criteriaQuery, Pageable pageable) {
        if (criteriaQuery == null || pageable == null) {
            return;
        }
        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate restrictions = criteriaQuery.getRestriction() != null ? criteriaQuery.getRestriction() : criteriaBuilder.conjunction();
        if (StringUtils.isNotEmpty(pageable.getSearchProperty()) && StringUtils.isNotEmpty(pageable.getSearchValue())) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get(pageable.getSearchProperty()), "%" + pageable.getSearchValue() + "%"));
        }
        if (pageable.getFilters() != null) {
            for (Filter filter : pageable.getFilters()) {
                if (filter == null || StringUtils.isEmpty(filter.getProperty())) {
                    continue;
                }
                if (filter.getOperator() == Filter.Operator.eq && filter.getValue() != null) {
                    if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(criteriaBuilder.lower(root.get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue()));
                    }
                } else if (filter.getOperator() == Filter.Operator.ne && filter.getValue() != null) {
                    if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue()));
                    }
                } else if (filter.getOperator() == Filter.Operator.gt && filter.getValue() != null) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if (filter.getOperator() == Filter.Operator.lt && filter.getValue() != null) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if (filter.getOperator() == Filter.Operator.ge && filter.getValue() != null) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if (filter.getOperator() == Filter.Operator.le && filter.getValue() != null) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get(filter.getProperty()), (Number) filter.getValue()));
                } else if (filter.getOperator() == Filter.Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get(filter.getProperty()), (String) filter.getValue()));
                } else if (filter.getOperator() == Filter.Operator.in && filter.getValue() != null) {
                    restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).in(filter.getValue()));
                } else if (filter.getOperator() == Filter.Operator.isNull) {
                    restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).isNull());
                } else if (filter.getOperator() == Filter.Operator.isNotNull) {
                    restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).isNotNull());
                }
            }
        }
        criteriaQuery.where(restrictions);
    }

    private void addOrders(CriteriaQuery<T> criteriaQuery, List<Order> orders) {
        if (criteriaQuery == null || orders == null || orders.isEmpty()) {
            return;
        }
        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
        if (!criteriaQuery.getOrderList().isEmpty()) {
            orderList.addAll(criteriaQuery.getOrderList());
        }
        for (Order order : orders) {
            if (order.getDirection() == Order.Direction.asc) {
                orderList.add(criteriaBuilder.asc(root.get(order.getProperty())));
            } else if (order.getDirection() == Order.Direction.desc) {
                orderList.add(criteriaBuilder.desc(root.get(order.getProperty())));
            }
        }
        criteriaQuery.orderBy(orderList);
    }

    private void addOrders(CriteriaQuery<T> criteriaQuery, Pageable pageable) {
        if (criteriaQuery == null || pageable == null) {
            return;
        }
        Root<T> root = getRoot(criteriaQuery);
        if (root == null) {
            return;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
        if (!criteriaQuery.getOrderList().isEmpty()) {
            orderList.addAll(criteriaQuery.getOrderList());
        }
        if (StringUtils.isNotEmpty(pageable.getOrderProperty()) && pageable.getOrderDirection() != null) {
            if (pageable.getOrderDirection() == Order.Direction.asc) {
                orderList.add(criteriaBuilder.asc(root.get(pageable.getOrderProperty())));
            } else if (pageable.getOrderDirection() == Order.Direction.desc) {
                orderList.add(criteriaBuilder.desc(root.get(pageable.getOrderProperty())));
            }
        }
        if (pageable.getOrders() != null) {
            for (Order order : pageable.getOrders()) {
                if (order.getDirection() == Order.Direction.asc) {
                    orderList.add(criteriaBuilder.asc(root.get(order.getProperty())));
                } else if (order.getDirection() == Order.Direction.desc) {
                    orderList.add(criteriaBuilder.desc(root.get(order.getProperty())));
                }
            }
        }
        criteriaQuery.orderBy(orderList);
    }

    private List<Filter> parseFiler(String filters) {
        List<Filter> filterList = new ArrayList<>();
        if (StringUtils.isNotEmpty(filters)) {
            String[] expressions = filters.split(";");
            for (String expression : expressions) {
                if (expression.contains(">=")) {
                    String[] fieldValue = expression.split(">=");
                    String field = fieldValue[0];
                    String value = fieldValue[1];
                    Object suitValue = suitValue(field, value);
                    filterList.add(Filter.ge(field, suitValue));
                } else if (expression.contains("<=")) {
                    String[] fieldValue = expression.split("<=");
                    String field = fieldValue[0];
                    String value = fieldValue[1];
                    Object suitValue = suitValue(field, value);
                    filterList.add(Filter.le(field, suitValue));
                } else if (expression.contains(">")) {
                    String[] fieldValue = expression.split(">");
                    String field = fieldValue[0];
                    String value = fieldValue[1];
                    Object suitValue = suitValue(field, value);
                    filterList.add(Filter.gt(field, suitValue));
                } else if (expression.contains("<")) {
                    String[] fieldValue = expression.split("<");
                    String field = fieldValue[0];
                    String value = fieldValue[1];
                    Object suitValue = suitValue(field, value);
                    filterList.add(Filter.lt(field, suitValue));
                } else if (expression.contains("!=")) {
                    String[] fieldValue = expression.split("!=");
                    String field = fieldValue[0];
                    String value = fieldValue[1];
                    if (!value.contains(",")) {
                        filterList.add(Filter.ne(field, suitValue(field, value)));
                    } else {
                        String [] values = value.split(",");
                        for (String s : values) {
                            filterList.add(Filter.ne(field, suitValue(field, s)));
                        }
                    }
                } else if (expression.contains("=")) {
                    String[] fieldValue = expression.split("=");
                    String field = fieldValue[0];
                    String value = fieldValue[1];
                    if (!value.contains(",")) {
                        filterList.add(Filter.eq(field, suitValue(field, value)));
                    } else {
                        String [] values = value.split(",");
                        Object [] suitValues = new Object[values.length];
                        for (int i = 0; i < values.length; i ++) {
                            suitValues[i] = suitValue(field, values[i]);
                        }
                        filterList.add(Filter.in(field, suitValues));
                    }
                } else if (expression.contains("?")) {
                    String[] fieldValue = expression.split("\\?");
                    filterList.add(Filter.like(fieldValue[0], "%" + fieldValue[1] + "%"));
                } else if (expression.contains("+")) {
                    String[] fieldValue = expression.split("\\+");
                    filterList.add(Filter.isNotNull(fieldValue[0]));
                } else if (expression.contains("-")) {
                    String[] fieldValue = expression.split("-");
                    filterList.add(Filter.isNull(fieldValue[0]));
                }
            }
        }
        return filterList;
    }

    private List<Order> parserOrder(Sort sort) {
        List<Order> orderList = new ArrayList<>();
        if (sort != null) {
            sort.forEach(item -> {
                if (item.getDirection().isAscending()) {
                    orderList.add(Order.asc(item.getProperty()));
                } else {
                    orderList.add(Order.desc(item.getProperty()));
                }
            });
        }
        return orderList;
    }

    /**
     * 将字符串值转换为实体相关字段对应类型的值
     * @param fieldName
     * @param strVal
     * @return
     */
    protected Object suitValue(String fieldName, String strVal) {
        Class<?> tmpClazz = entityClass;
        for (; ; ) {
            try {
                Field field = tmpClazz.getDeclaredField(fieldName);
                if ("java.util.Date".equals(field.getType().getName())) {
                    if (DATE_PATTERN.matcher(strVal).find()) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return dateFormat.parse(strVal);
                    }
                    if (UTC_DATE_PATTERN.matcher(strVal).find()) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        return dateFormat.parse(strVal);
                    }
                }
                Constructor constructor = field.getType().getConstructor(String.class);
                return constructor.newInstance(strVal);
            } catch (Exception e) {
                if (e instanceof NoSuchFieldException) {
                    tmpClazz = tmpClazz.getSuperclass();
                    if (tmpClazz == null || tmpClazz == Object.class) {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                } else {
                    if (e.getCause() != null) {
                        throw new IllegalArgumentException(e.getCause().getMessage());
                    }
                    throw new IllegalArgumentException(e.getMessage());
                }
            }
        }
    }

}