package com.x.shop.dao.impl;

import com.x.shop.common.Filter;
import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.OrderDao;
import com.x.shop.entity.Order;
import com.x.shop.entity.OrderItem;
import com.x.shop.entity.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Dao - 订单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("orderDaoImpl")
public class OrderDaoImpl extends BaseDaoImpl<Order, Long> implements OrderDao {

    @Override
    public Order findBySn(String sn) {
        if (sn == null) {
            return null;
        }
        String jpql = "select orders from Order orders where lower(orders.sn) = lower(:sn)";
        try {
            return entityManager.createQuery(jpql, Order.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Order> findList(Integer count, List<Filter> filters, List<com.x.shop.common.Order> orders) {
        //TODO
        /*if (member == null) {
            return Collections.<Order>emptyList();
        }*/
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        //criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    @Override
    public Page<Order> findPage(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (orderStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
        }
        if (paymentStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
        }
        if (shippingStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
        }
        if (hasExpired != null) {
            if (hasExpired) {
                restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
            }
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public Long count(Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (orderStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
        }
        if (paymentStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
        }
        if (shippingStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
        }
        if (hasExpired != null) {
            if (hasExpired) {
                restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
            }
        }
        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery, null);
    }

    @Override
    public Long waitingPaymentCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("orderStatus"), Order.OrderStatus.completed), criteriaBuilder.notEqual(root.get("orderStatus"), Order.OrderStatus.cancelled));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("paymentStatus"), Order.PaymentStatus.unpaid), criteriaBuilder.equal(root.get("paymentStatus"), Order.PaymentStatus.partialPayment)));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
        /*if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }*/
        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery, null);
    }

    @Override
    public Long waitingShippingCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("orderStatus"), Order.OrderStatus.completed), criteriaBuilder.notEqual(root.get("orderStatus"), Order.OrderStatus.cancelled), criteriaBuilder.equal(root.get("paymentStatus"), Order.PaymentStatus.paid), criteriaBuilder.equal(root.get("shippingStatus"), Order.ShippingStatus.unshipped));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
        /*if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }*/
        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery, null);
    }

    @Override
    public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.sum(root.<BigDecimal>get("amountPaid")));
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.completed));
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        criteriaQuery.where(restrictions);
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
    }

    @Override
    public Integer getSalesVolume(Date beginDate, Date endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.sum(root.join("orderItems").<Integer>get("shippedQuantity")));
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.completed));
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        criteriaQuery.where(restrictions);
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
    }

    @Override
    public void releaseStock() {
        String jpql = "select orders from Order orders where orders.isAllocatedStock = :isAllocatedStock and orders.expire is not null and orders.expire <= :now";
        List<Order> orders = entityManager.createQuery(jpql, Order.class).setParameter("isAllocatedStock", true).setParameter("now", new Date()).getResultList();
        if (orders != null) {
            for (Order order : orders) {
                if (order != null && order.getOrderItems() != null) {
                    for (OrderItem orderItem : order.getOrderItems()) {
                        if (orderItem != null) {
                            Product product = orderItem.getProduct();
                            if (product != null) {
                                entityManager.lock(product, LockModeType.PESSIMISTIC_WRITE);
                                product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
                            }
                        }
                    }
                    order.setIsAllocatedStock(false);
                }
            }
        }
    }

}