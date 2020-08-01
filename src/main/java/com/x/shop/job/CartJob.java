package com.x.shop.job;

import com.x.shop.service.CartService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Job - 购物车
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("cartJob")
@Lazy(false)
public class CartJob {

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    /**
     * 清除过期购物车
     */
    @Scheduled(cron = "${job.cart_evict_expired.cron}")
    public void evictExpired() {
        cartService.evictExpired();
    }

}