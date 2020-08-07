package com.x.shop.controller.shop;

import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.entity.*;
import com.x.shop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Controller - 商品
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopProductController")
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    /**
     * 首页
     */
    @RequestMapping(value = "/{sn}/index.html", method = RequestMethod.GET)
    public String index(@PathVariable String sn, ModelMap model) {
        Product product = productService.findBySn(sn);
        if (product == null) {
            return RESOURCE_NOT_FOUND_VIEW;
        }
        Page page =  new Page<>(Collections.singletonList(product), 1, new Pageable(1, 10));
        model.addAttribute("product", product);
        model.addAttribute("page", page);
        return "/shop/product/index";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/{sn}/list.html", method = RequestMethod.GET)
    public String list(@PathVariable String sn, ModelMap model) {
        Product product = productService.findBySn(sn);
        if (product == null) {
            return RESOURCE_NOT_FOUND_VIEW;
        }
        Page page =  new Page<>(Collections.singletonList(product), 1, new Pageable(1, 10));
        model.addAttribute("product", product);
        model.addAttribute("page", page);
        model.addAttribute("productCategory", product.getProductCategory());
        return "/shop/product/list";
    }

    /**
     * 点击数
     */
    @RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Long hits(@PathVariable Long id) {
        return productService.viewHits(id);
    }

}