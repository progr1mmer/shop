package com.x.shop.template.directive;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Product;
import com.x.shop.entity.Review;
import com.x.shop.service.ProductService;
import com.x.shop.service.ReviewService;
import com.x.shop.util.FreemarkerUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模板指令 - 评论
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("reviewListDirective")
public class ReviewListDirective extends BaseDirective {

    /**
     * "会员ID"参数名称
     */
    private static final String MEMBER_ID_PARAMETER_NAME = "memberId";

    /**
     * "商品ID"参数名称
     */
    private static final String PRODUCT_ID_PARAMETER_NAME = "productId";

    /**
     * "类型"参数名称
     */
    private static final String TYPE_PARAMETER_NAME = "type";

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "reviews";

    @Resource(name = "reviewServiceImpl")
    private ReviewService reviewService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Long memberId = FreemarkerUtils.getParameter(MEMBER_ID_PARAMETER_NAME, Long.class, params);
        Long productId = FreemarkerUtils.getParameter(PRODUCT_ID_PARAMETER_NAME, Long.class, params);
        Review.Type type = FreemarkerUtils.getParameter(TYPE_PARAMETER_NAME, Review.Type.class, params);

        //Member member = memberService.find(memberId);
        Product product = productService.find(productId);

        List<Review> reviews;
        //TODO
        //if ((memberId != null && member == null) || (productId != null && product == null)) {
        if (memberId != null || (productId != null && product == null)) {
            reviews = new ArrayList<>();
        } else {
            boolean useCache = useCache(env, params);
            String cacheRegion = getCacheRegion(env, params);
            Integer count = getCount(params);
            List<Filter> filters = getFilters(params, Review.class);
            List<Order> orders = getOrders(params);
            if (useCache) {
                reviews = reviewService.findList(product, type, true, count, filters, orders, cacheRegion);
            } else {
                reviews = reviewService.findList(product, type, true, count, filters, orders);
            }
        }
        setLocalVariable(VARIABLE_NAME, reviews, env, body);
    }

}