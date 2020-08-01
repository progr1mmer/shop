package com.x.shop.service.impl;

import com.x.shop.dao.PaymentDao;
import com.x.shop.entity.Order;
import com.x.shop.entity.Payment;
import com.x.shop.entity.Payment.Status;
import com.x.shop.entity.Payment.Type;
import com.x.shop.service.OrderService;
import com.x.shop.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.util.Date;

/**
 * Service - 收款单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {

    @Resource(name = "paymentDaoImpl")
    private PaymentDao paymentDao;
    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Override
    @Transactional(readOnly = true)
    public Payment findBySn(String sn) {
        return paymentDao.findBySn(sn);
    }

    @Override
    public void handle(Payment payment) {
        paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
        if (payment != null && payment.getStatus() == Status.wait) {
            if (payment.getType() == Type.payment) {
                Order order = payment.getOrder();
                if (order != null) {
                    orderService.payment(order, payment, null);
                }
            }
            payment.setStatus(Status.success);
            payment.setPaymentDate(new Date());
            paymentDao.merge(payment);
        }
    }

}