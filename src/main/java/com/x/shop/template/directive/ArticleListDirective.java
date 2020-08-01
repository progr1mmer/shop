package com.x.shop.template.directive;

import com.x.shop.common.Filter;
import com.x.shop.common.Order;
import com.x.shop.entity.Article;
import com.x.shop.entity.ArticleCategory;
import com.x.shop.entity.Tag;
import com.x.shop.service.ArticleCategoryService;
import com.x.shop.service.ArticleService;
import com.x.shop.service.TagService;
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
 * 模板指令 - 文章列表
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("articleListDirective")
public class ArticleListDirective extends BaseDirective {

    /**
     * "文章分类ID"参数名称
     */
    private static final String ARTICLE_CATEGORY_ID_PARAMETER_NAME = "articleCategoryId";

    /**
     * "标签ID"参数名称
     */
    private static final String TAG_IDS_PARAMETER_NAME = "tagIds";

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "articles";

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;
    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;
    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Long articleCategoryId = FreemarkerUtils.getParameter(ARTICLE_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
        Long[] tagIds = FreemarkerUtils.getParameter(TAG_IDS_PARAMETER_NAME, Long[].class, params);

        ArticleCategory articleCategory = articleCategoryService.find(articleCategoryId);
        List<Tag> tags = tagService.findList(tagIds);

        List<Article> articles;
        if ((articleCategoryId != null && articleCategory == null) || (tagIds != null && tags.isEmpty())) {
            articles = new ArrayList<Article>();
        } else {
            boolean useCache = useCache(env, params);
            String cacheRegion = getCacheRegion(env, params);
            Integer count = getCount(params);
            List<Filter> filters = getFilters(params, Article.class);
            List<Order> orders = getOrders(params);
            if (useCache) {
                articles = articleService.findList(articleCategory, tags, count, filters, orders, cacheRegion);
            } else {
                articles = articleService.findList(articleCategory, tags, count, filters, orders);
            }
        }
        setLocalVariable(VARIABLE_NAME, articles, env, body);
    }

}