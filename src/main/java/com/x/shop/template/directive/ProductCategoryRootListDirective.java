package com.x.shop.template.directive;

import com.x.shop.entity.ProductCategory;
import com.x.shop.service.ProductCategoryService;
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
 * 模板指令 - 顶级商品分类列表
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("productCategoryRootListDirective")
public class ProductCategoryRootListDirective extends BaseDirective {

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "productCategories";

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        List<ProductCategory> productCategories;
        boolean useCache = useCache(env, params);
        String cacheRegion = getCacheRegion(env, params);
        Integer count = getCount(params);
        if (useCache) {
            productCategories = productCategoryService.findRoots(count, cacheRegion);
        } else {
            productCategories = productCategoryService.findRoots(count);
        }
        setLocalVariable(VARIABLE_NAME, productCategories, env, body);
    }

}