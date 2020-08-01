package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Navigation;
import com.x.shop.service.ArticleCategoryService;
import com.x.shop.service.NavigationService;
import com.x.shop.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 导航
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminNavigationController")
@RequestMapping("/admin/navigation")
public class NavigationController extends BaseController {

    @Resource(name = "navigationServiceImpl")
    private NavigationService navigationService;
    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;
    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("positions", Navigation.Position.values());
        model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        return "admin/navigation/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Navigation navigation, RedirectAttributes redirectAttributes) {
        if (!isValid(navigation)) {
            return ERROR_VIEW;
        }
        navigationService.save(navigation);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("positions", Navigation.Position.values());
        model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("navigation", navigationService.find(id));
        return "admin/navigation/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Navigation navigation, RedirectAttributes redirectAttributes) {
        if (!isValid(navigation)) {
            return ERROR_VIEW;
        }
        navigationService.update(navigation);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("topNavigations", navigationService.findList(Navigation.Position.top));
        model.addAttribute("middleNavigations", navigationService.findList(Navigation.Position.middle));
        model.addAttribute("bottomNavigations", navigationService.findList(Navigation.Position.bottom));
        return "admin/navigation/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        navigationService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}