package com.x.shop.template.directive;

import com.x.shop.entity.ArticleCategory;
import com.x.shop.service.ArticleCategoryService;
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
 * 模板指令 - 上级文章分类列表
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("articleCategoryParentListDirective")
public class ArticleCategoryParentListDirective extends BaseDirective {
    /**
     * "文章分类ID"参数名称
     */
    private static final String ARTICLE_CATEGORY_ID_PARAMETER_NAME = "articleCategoryId";

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "articleCategories";

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Long articleCategoryId = FreemarkerUtils.getParameter(ARTICLE_CATEGORY_ID_PARAMETER_NAME, Long.class, params);

        ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);

        List<ArticleCategory> articleCategories;
        if (articleCategoryId != null && articleCategory == null) {
            articleCategories = new ArrayList<ArticleCategory>();
        } else {
            boolean useCache = useCache(env, params);
            String cacheRegion = getCacheRegion(env, params);
            Integer count = getCount(params);
            if (useCache) {
                articleCategories = articleCategoryService.findParents(articleCategory, count, cacheRegion);
            } else {
                articleCategories = articleCategoryService.findParents(articleCategory, count);
            }
        }
        setLocalVariable(VARIABLE_NAME, articleCategories, env, body);
    }

}