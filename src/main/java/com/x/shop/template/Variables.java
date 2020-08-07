package com.x.shop.template;

import com.jagregory.shiro.freemarker.ShiroTags;
import com.x.shop.common.Setting;
import com.x.shop.template.directive.*;
import com.x.shop.template.method.AbbreviateMethod;
import com.x.shop.template.method.CurrencyMethod;
import com.x.shop.template.method.MessageMethod;
import com.x.shop.util.SettingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * @author progr1mmer.
 * @date Created on 2020/2/6.
 */
@Configuration
@Lazy(value = false)
public class Variables {

    @Autowired
    private freemarker.template.Configuration configuration;

    private Setting setting = SettingUtils.get();
    @Value("${server.servlet.context-path:/}")
    private String contextPath;
    @Value("${spring.freemarker.settings.locale}")
    private String locale;
    @Autowired
    private MessageMethod messageMethod;
    @Autowired
    private AbbreviateMethod abbreviateMethod;
    @Autowired
    private CurrencyMethod currencyMethod;
    @Autowired
    private ExecuteTimeDirective executeTimeDirective;
    @Autowired
    private FlashMessageDirective flashMessageDirective;
    @Autowired
    private PaginationDirective paginationDirective;
    @Autowired
    private SeoDirective seoDirective;
    @Autowired
    private NavigationListDirective navigationListDirective;
    @Autowired
    private TagListDirective tagListDirective;
    @Autowired
    private FriendLinkListDirective friendLinkListDirective;
    @Autowired
    private BrandListDirective brandListDirective;
    @Autowired
    private ArticleListDirective articleListDirective;
    @Autowired
    private ArticleCategoryRootListDirective articleCategoryRootListDirective;
    @Autowired
    private ArticleCategoryParentListDirective articleCategoryParentListDirective;
    @Autowired
    private ArticleCategoryChildrenListDirective articleCategoryChildrenListDirective;
    @Autowired
    private ProductListDirective productListDirective;
    @Autowired
    private ProductCategoryRootListDirective productCategoryRootListDirective;
    @Autowired
    private ProductCategoryParentListDirective productCategoryParentListDirective;
    @Autowired
    private ProductCategoryChildrenListDirective productCategoryChildrenListDirective;
    @Autowired
    private PromotionListDirective promotionListDirective;

    @PostConstruct
    public void setVariables() throws Exception {
        configuration.setSharedVariable("base", "/".equals(contextPath) ? "" : contextPath);
        configuration.setSharedVariable("locale", locale);
        configuration.setSharedVariable("setting", setting);
        configuration.setSharedVariable("message", messageMethod);
        configuration.setSharedVariable("abbreviate", abbreviateMethod);
        configuration.setSharedVariable("currency", currencyMethod);
        configuration.setSharedVariable("execute_time", executeTimeDirective);
        configuration.setSharedVariable("flash_message", flashMessageDirective);
        configuration.setSharedVariable("pagination", paginationDirective);
        configuration.setSharedVariable("seo", seoDirective);
        configuration.setSharedVariable("navigation_list", navigationListDirective);
        configuration.setSharedVariable("tag_list", tagListDirective);
        configuration.setSharedVariable("friend_link_list", friendLinkListDirective);
        configuration.setSharedVariable("brand_list", brandListDirective);
        configuration.setSharedVariable("article_list", articleListDirective);
        configuration.setSharedVariable("article_category_root_list", articleCategoryRootListDirective);
        configuration.setSharedVariable("article_category_parent_list", articleCategoryParentListDirective);
        configuration.setSharedVariable("article_category_children_list", articleCategoryChildrenListDirective);
        configuration.setSharedVariable("product_list", productListDirective);
        configuration.setSharedVariable("product_category_root_list", productCategoryRootListDirective);
        configuration.setSharedVariable("product_category_parent_list", productCategoryParentListDirective);
        configuration.setSharedVariable("product_category_children_list", productCategoryChildrenListDirective);
        configuration.setSharedVariable("promotion_list", promotionListDirective);
        configuration.setSharedVariable("shiro", new ShiroTags());
    }

}
