package com.x.shop.template.directive;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Promotion;
import com.x.shop.service.PromotionService;
import com.x.shop.util.FreemarkerUtils;
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
 * 模板指令 - 促销列表
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("promotionListDirective")
public class PromotionListDirective extends BaseDirective {

    /**
     * "是否已开始"参数名称
     */
    private static final String HAS_BEGUN_PARAMETER_NAME = "hasBegun";

    /**
     * "是否已结束"参数名称
     */
    private static final String HAS_ENDED_PARAMETER_NAME = "hasEnded";

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "promotions";

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Boolean hasBegun = FreemarkerUtils.getParameter(HAS_BEGUN_PARAMETER_NAME, Boolean.class, params);
        Boolean hasEnded = FreemarkerUtils.getParameter(HAS_ENDED_PARAMETER_NAME, Boolean.class, params);

        List<Promotion> promotions;
        boolean useCache = useCache(env, params);
        String cacheRegion = getCacheRegion(env, params);
        Integer count = getCount(params);
        List<Filter> filters = getFilters(params, Promotion.class);
        List<Order> orders = getOrders(params);
        if (useCache) {
            promotions = promotionService.findList(hasBegun, hasEnded, count, filters, orders, cacheRegion);
        } else {
            promotions = promotionService.findList(hasBegun, hasEnded, count, filters, orders);
        }
        setLocalVariable(VARIABLE_NAME, promotions, env, body);
    }

}