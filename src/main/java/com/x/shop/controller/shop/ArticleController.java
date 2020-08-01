package com.x.shop.controller.shop;

import com.x.shop.common.Pageable;
import com.x.shop.common.ResourceNotFoundException;
import com.x.shop.entity.ArticleCategory;
import com.x.shop.service.ArticleCategoryService;
import com.x.shop.service.ArticleService;
import com.x.shop.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Controller - 文章
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopArticleController")
@RequestMapping("/article")
public class ArticleController extends BaseController {

    /**
     * 每页记录数
     */
    private static final int PAGE_SIZE = 20;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;
    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;
    @Resource(name = "searchServiceImpl")
    private SearchService searchService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list/{id}.html", method = RequestMethod.GET)
    public String list(@PathVariable Long id, Integer pageNumber, ModelMap model) {
        ArticleCategory articleCategory = articleCategoryService.find(id);
        if (articleCategory == null) {
            throw new ResourceNotFoundException();
        }
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        model.addAttribute("articleCategory", articleCategory);
        model.addAttribute("page", articleService.findPage(articleCategory, null, pageable));
        return "shop/article/list";
    }

    /**
     * 搜索
     */
    @RequestMapping(value = "/search.html", method = RequestMethod.GET)
    public String search(String keyword, Integer pageNumber, ModelMap model) {
        if (StringUtils.isEmpty(keyword)) {
            return ERROR_VIEW;
        }
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        model.addAttribute("articleKeyword", keyword);
        model.addAttribute("page", searchService.search(keyword, pageable));
        return "shop/article/search";
    }

    /**
     * 点击数
     */
    @RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Long hits(@PathVariable Long id) {
        return articleService.viewHits(id);
    }

}