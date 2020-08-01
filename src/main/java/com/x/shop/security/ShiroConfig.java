package com.x.shop.security;

import com.x.shop.service.AdminService;
import com.x.shop.service.CaptchaService;
import com.x.shop.service.RSAService;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author progr1mmer.
 * @date Created on 2020/2/12.
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private CacheManager cacheManager;

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Bean
    EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    @Bean
    SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(new AuthenticationRealm(captchaService, adminService));
        defaultWebSecurityManager.setCacheManager(ehCacheManager());
        return defaultWebSecurityManager;
    }

    @Bean("shiroFilter")
    ShiroFilterFactoryBean shiroFilterFactoryBean() throws IOException {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/admin/login.html");
        shiroFilterFactoryBean.setSuccessUrl("/admin/common/main.html");
        shiroFilterFactoryBean.setUnauthorizedUrl("/admin/common/unauthorized.html");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/admin/login.html", "authc");
        filterChainDefinitionMap.put("/admin/logout.html", "logout");
        filterChainDefinitionMap.put("/admin/css/**", "anon");
        filterChainDefinitionMap.put("/admin/images/**", "anon");
        filterChainDefinitionMap.put("/admin/js/**", "anon");
        filterChainDefinitionMap.put("/admin/index.html", "anon");
        filterChainDefinitionMap.put("/admin/common/captcha.html", "anon");

        filterChainDefinitionMap.put("/admin/product/**", "perms[\"admin:product\"]");
        filterChainDefinitionMap.put("/admin/product_category/**", "perms[\"admin:productCategory\"]");
        filterChainDefinitionMap.put("/admin/parameter_group/**", "perms[\"admin:parameterGroup\"]");
        filterChainDefinitionMap.put("/admin/attribute/**", "perms[\"admin:attribute\"]");
        filterChainDefinitionMap.put("/admin/specification/**", "perms[\"admin:specification\"]");
        filterChainDefinitionMap.put("/admin/brand/**", "perms[\"admin:brand\"]");
        filterChainDefinitionMap.put("/admin/product_notify/**", "perms[\"admin:productNotify\"]");

        filterChainDefinitionMap.put("/admin/order/**", "perms[\"admin:order\"]");
        filterChainDefinitionMap.put("/admin/print/**", "perms[\"admin:print\"]");
        filterChainDefinitionMap.put("/admin/payment/**", "perms[\"admin:payment\"]");
        filterChainDefinitionMap.put("/admin/refunds/**", "perms[\"admin:refunds\"]");
        filterChainDefinitionMap.put("/admin/shipping/**", "perms[\"admin:shipping\"]");
        filterChainDefinitionMap.put("/admin/returns/**", "perms[\"admin:returns\"]");
        filterChainDefinitionMap.put("/admin/delivery_center/**", "perms[\"admin:deliveryCenter\"]");
        filterChainDefinitionMap.put("/admin/delivery_template/**", "perms[\"admin:deliveryTemplate\"]");

        filterChainDefinitionMap.put("/admin/member/**", "perms[\"admin:member\"]");
        filterChainDefinitionMap.put("/admin/member_rank/**", "perms[\"admin:memberRank\"]");
        filterChainDefinitionMap.put("/admin/member_attribute/**", "perms[\"admin:memberAttribute\"]");
        filterChainDefinitionMap.put("/admin/review/**", "perms[\"admin:review\"]");
        filterChainDefinitionMap.put("/admin/consultation/**", "perms[\"admin:consultation\"]");

        filterChainDefinitionMap.put("/admin/navigation/**", "perms[\"admin:navigation\"]");
        filterChainDefinitionMap.put("/admin/article/**", "perms[\"admin:article\"]");
        filterChainDefinitionMap.put("/admin/article_category/**", "perms[\"admin:articleCategory\"]");
        filterChainDefinitionMap.put("/admin/tag/**", "perms[\"admin:tag\"]");
        filterChainDefinitionMap.put("/admin/friend_link/**", "perms[\"admin:friendLink\"]");
        filterChainDefinitionMap.put("/admin/ad_position/**", "perms[\"admin:adPosition\"]");
        filterChainDefinitionMap.put("/admin/ad/**", "perms[\"admin:ad\"]");
        filterChainDefinitionMap.put("/admin/template/**", "perms[\"admin:template\"]");
        filterChainDefinitionMap.put("/admin/cache/**", "perms[\"admin:cache\"]");
        filterChainDefinitionMap.put("/admin/static/**", "perms[\"admin:static\"]");
        filterChainDefinitionMap.put("/admin/index/**", "perms[\"admin:index\"]");

        filterChainDefinitionMap.put("/admin/promotion/**", "perms[\"admin:promotion\"]");
        filterChainDefinitionMap.put("/admin/coupon/**", "perms[\"admin:coupon\"]");
        filterChainDefinitionMap.put("/admin/seo/**", "perms[\"admin:seo\"]");
        filterChainDefinitionMap.put("/admin/sitemap/**", "perms[\"admin:sitemap\"]");

        filterChainDefinitionMap.put("/admin/statistics/**", "perms[\"admin:statistics\"]");
        filterChainDefinitionMap.put("/admin/sales/**", "perms[\"admin:sales\"]");
        filterChainDefinitionMap.put("/admin/sales_ranking/**", "perms[\"admin:salesRanking\"]");
        filterChainDefinitionMap.put("/admin/purchase_ranking/**", "perms[\"admin:purchaseRanking\"]");
        filterChainDefinitionMap.put("/admin/deposit/**", "perms[\"admin:deposit\"]");

        filterChainDefinitionMap.put("/admin/setting/**", "perms[\"admin:setting\"]");
        filterChainDefinitionMap.put("/admin/area/**", "perms[\"admin:area\"]");
        filterChainDefinitionMap.put("/admin/payment_method/**", "perms[\"admin:paymentMethod\"]");
        filterChainDefinitionMap.put("/admin/shipping_method/**", "perms[\"admin:shippingMethod\"]");
        filterChainDefinitionMap.put("/admin/delivery_corp/**", "perms[\"admin:deliveryCorp\"]");
        filterChainDefinitionMap.put("/admin/payment_plugin/**", "perms[\"admin:paymentPlugin\"]");
        filterChainDefinitionMap.put("/admin/storage_plugin/**", "perms[\"admin:storagePlugin\"]");
        filterChainDefinitionMap.put("/admin/admin/**", "perms[\"admin:admin\"]");
        filterChainDefinitionMap.put("/admin/role/**", "perms[\"admin:role\"]");
        filterChainDefinitionMap.put("/admin/message/**", "perms[\"admin:message\"]");
        filterChainDefinitionMap.put("/admin/log/**", "perms[\"admin:log\"]");

        filterChainDefinitionMap.put("/admin/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new AuthenticationFilter(rsaService));
        shiroFilterFactoryBean.setFilters(filterMap);
        return shiroFilterFactoryBean;
    }

    /*@Bean
    FilterRegistrationBean<Filter> filterProxyFilterRegistrationBean() throws Exception {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/admin/**"));
        return filterRegistrationBean;
    }*/
}
