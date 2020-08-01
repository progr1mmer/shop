package com.x.shop.controller.shop;

import com.x.shop.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 商品分类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopProductCategoryController")
public class ProductCategoryController extends BaseController {

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    /**
     * 首页
     */
    @RequestMapping(value = "/product_category.html", method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("rootProductCategories", productCategoryService.findRoots());
        return "/shop/product_category/index";
    }

}