package com.x.shop.template.directive;

import com.x.shop.common.Filter;
import com.x.shop.entity.*;
import com.x.shop.service.*;
import com.x.shop.util.FreemarkerUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 模板指令 - 商品列表
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("productListDirective")
@Lazy(value = false)
public class ProductListDirective extends BaseDirective {

    /**
     * "商品分类ID"参数名称
     */
    private static final String PRODUCT_CATEGORY_ID_PARAMETER_NAME = "productCategoryId";

    /**
     * "品牌ID"参数名称
     */
    private static final String BRAND_ID_PARAMETER_NAME = "brandId";

    /**
     * "促销ID"参数名称
     */
    private static final String PROMOTION_ID_PARAMETER_NAME = "promotionId";

    /**
     * "标签ID"参数名称
     */
    private static final String TAG_IDS_PARAMETER_NAME = "tagIds";

    /**
     * "属性值"参数名称
     */
    private static final String ATTRIBUTE_VALUE_PARAMETER_NAME = "attributeValue";

    /**
     * "最低价格"参数名称
     */
    private static final String START_PRICE_PARAMETER_NAME = "startPrice";

    /**
     * "最高价格"参数名称
     */
    private static final String END_PRICE_PARAMETER_NAME = "endPrice";

    /**
     * "排序类型"参数名称
     */
    private static final String ORDER_TYPE_PARAMETER_NAME = "orderType";

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "products";

    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;
    @Resource(name = "brandServiceImpl")
    private BrandService brandService;
    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;
    @Resource(name = "attributeServiceImpl")
    private AttributeService attributeService;
    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Long productCategoryId = FreemarkerUtils.getParameter(PRODUCT_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
        Long brandId = FreemarkerUtils.getParameter(BRAND_ID_PARAMETER_NAME, Long.class, params);
        Long promotionId = FreemarkerUtils.getParameter(PROMOTION_ID_PARAMETER_NAME, Long.class, params);
        Long[] tagIds = FreemarkerUtils.getParameter(TAG_IDS_PARAMETER_NAME, Long[].class, params);
        Map<Long, String> attributeValue = FreemarkerUtils.getParameter(ATTRIBUTE_VALUE_PARAMETER_NAME, Map.class, params);
        BigDecimal startPrice = FreemarkerUtils.getParameter(START_PRICE_PARAMETER_NAME, BigDecimal.class, params);
        BigDecimal endPrice = FreemarkerUtils.getParameter(END_PRICE_PARAMETER_NAME, BigDecimal.class, params);
        Product.OrderType orderType = FreemarkerUtils.getParameter(ORDER_TYPE_PARAMETER_NAME, Product.OrderType.class, params);

        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        Brand brand = brandService.find(brandId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagIds);
        Map<Attribute, String> attributeValueMap = new HashMap<Attribute, String>();
        if (attributeValue != null) {
            for (Entry<Long, String> entry : attributeValue.entrySet()) {
                Attribute attribute = attributeService.find(entry.getKey());
                if (attribute != null) {
                    attributeValueMap.put(attribute, entry.getValue());
                }
            }
        }

        List<Product> products;
        if ((productCategoryId != null && productCategory == null) || (brandId != null && brand == null) || (promotionId != null && promotion == null) || (tagIds != null && tags.isEmpty()) || (attributeValue != null && attributeValueMap.isEmpty())) {
            products = new ArrayList<>();
        } else {
            boolean useCache = useCache(env, params);
            String cacheRegion = getCacheRegion(env, params);
            Integer count = getCount(params);
            List<Filter> filters = getFilters(params, Article.class);
            List<com.x.shop.common.Order> orders = getOrders(params);
            if (useCache) {
                products = productService.findList(productCategory, brand, promotion, tags, attributeValueMap, startPrice, endPrice, true, true, null, false, null, null, orderType, count, filters, orders, cacheRegion);
            } else {
                products = productService.findList(productCategory, brand, promotion, tags, attributeValueMap, startPrice, endPrice, true, true, null, false, null, null, orderType, count, filters, orders);
            }
        }
        setLocalVariable(VARIABLE_NAME, products, env, body);
    }

}