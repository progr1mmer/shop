package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Article;
import com.x.shop.entity.Tag;
import com.x.shop.service.ArticleCategoryService;
import com.x.shop.service.ArticleService;
import com.x.shop.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashSet;

/**
 * Controller - 文章
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminArticleController")
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;
    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;
    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
        model.addAttribute("tags", tagService.findList(Tag.Type.article));
        return "admin/article/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Article article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes) {
        article.setArticleCategory(articleCategoryService.find(articleCategoryId));
        article.setTags(new HashSet<>(tagService.findList(tagIds)));
        if (!isValid(article)) {
            return ERROR_VIEW;
        }
        article.setHits(0L);
        article.setPageNumber(null);
        articleService.save(article);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
        model.addAttribute("tags", tagService.findList(Tag.Type.article));
        model.addAttribute("article", articleService.find(id));
        return "admin/article/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Article article, Long articleCategoryId, Long[] tagIds, RedirectAttributes redirectAttributes) {
        article.setArticleCategory(articleCategoryService.find(articleCategoryId));
        article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        if (!isValid(article)) {
            return ERROR_VIEW;
        }
        articleService.update(article, "hits", "pageNumber");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", articleService.findPage(pageable));
        return "admin/article/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        articleService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}