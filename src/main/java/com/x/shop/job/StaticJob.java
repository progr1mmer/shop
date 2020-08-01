package com.x.shop.job;

import com.x.shop.service.StaticService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Job - 静态化
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("staticJob")
@Lazy(false)
public class StaticJob {

    @Resource(name = "staticServiceImpl")
    private StaticService staticService;

    /**
     * 生成静态
     */
    @Scheduled(cron = "${job.static_build.cron}")
    public void build() {
        staticService.buildAll();
    }

}