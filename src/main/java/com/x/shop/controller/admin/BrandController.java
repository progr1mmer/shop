package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Brand;
import com.x.shop.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 品牌
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminBrandController")
@RequestMapping("/admin/brand")
public class BrandController extends BaseController {

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("types", Brand.Type.values());
        return "admin/brand/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Brand brand, RedirectAttributes redirectAttributes) {
        if (!isValid(brand)) {
            return ERROR_VIEW;
        }
        if (brand.getType() == Brand.Type.text) {
            brand.setLogo(null);
        } else if (StringUtils.isEmpty(brand.getLogo())) {
            return ERROR_VIEW;
        }
        brand.setProducts(null);
        brand.setProductCategories(null);
        brand.setPromotions(null);
        brandService.save(brand);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("types", Brand.Type.values());
        model.addAttribute("brand", brandService.find(id));
        return "admin/brand/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Brand brand, RedirectAttributes redirectAttributes) {
        if (!isValid(brand)) {
            return ERROR_VIEW;
        }
        if (brand.getType() == Brand.Type.text) {
            brand.setLogo(null);
        } else if (StringUtils.isEmpty(brand.getLogo())) {
            return ERROR_VIEW;
        }
        brandService.update(brand, "products", "productCategories", "promotions");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", brandService.findPage(pageable));
        return "admin/brand/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        brandService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}