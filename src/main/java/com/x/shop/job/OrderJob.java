package com.x.shop.job;

import com.x.shop.service.OrderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Job - 订单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("orderJob")
@Lazy(false)
public class OrderJob {

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    /**
     * 释放过期订单库存
     */
    @Scheduled(cron = "${job.order_release_stock.cron}")
    public void releaseStock() {
        orderService.releaseStock();
    }

}