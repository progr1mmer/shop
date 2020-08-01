package com.x.shop.template.directive;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Navigation;
import com.x.shop.service.NavigationService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 模板指令 - 导航列表
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("navigationListDirective")
public class NavigationListDirective extends BaseDirective {

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "navigations";

    @Resource(name = "navigationServiceImpl")
    private NavigationService navigationService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        List<Navigation> navigations;
        boolean useCache = useCache(env, params);
        String cacheRegion = getCacheRegion(env, params);
        Integer count = getCount(params);
        List<Filter> filters = getFilters(params, Navigation.class);
        List<Order> orders = getOrders(params);
        if (useCache) {
            navigations = navigationService.findList(count, filters, orders, cacheRegion);
        } else {
            navigations = navigationService.findList(count, filters, orders);
        }
        setLocalVariable(VARIABLE_NAME, navigations, env, body);
    }

}