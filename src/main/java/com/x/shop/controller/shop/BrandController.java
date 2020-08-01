package com.x.shop.controller.shop;

import com.x.shop.common.Pageable;
import com.x.shop.common.ResourceNotFoundException;
import com.x.shop.entity.Brand;
import com.x.shop.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 品牌
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopBrandController")
@RequestMapping("/brand")
public class BrandController extends BaseController {

    /**
     * 每页记录数
     */
    private static final int PAGE_SIZE = 40;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list/{pageNumber}.html", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, ModelMap model) {
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        model.addAttribute("page", brandService.findPage(pageable));
        return "/shop/brand/list";
    }

    /**
     * 内容
     */
    @RequestMapping(value = "/content/{id}.html", method = RequestMethod.GET)
    public String content(@PathVariable Long id, ModelMap model) {
        Brand brand = brandService.find(id);
        if (brand == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("brand", brand);
        return "/shop/brand/content";
    }

}