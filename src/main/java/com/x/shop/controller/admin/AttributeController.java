package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Attribute;
import com.x.shop.entity.BaseEntity.Save;
import com.x.shop.entity.Product;
import com.x.shop.service.AttributeService;
import com.x.shop.service.ProductCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Iterator;

/**
 * Controller - 属性
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminAttributeController")
@RequestMapping("/admin/attribute")
public class AttributeController extends BaseController {

    @Resource(name = "attributeServiceImpl")
    private AttributeService attributeService;
    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("attributeValuePropertyCount", Product.ATTRIBUTE_VALUE_PROPERTY_COUNT);
        return "admin/attribute/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Attribute attribute, Long productCategoryId, RedirectAttributes redirectAttributes) {
        for (Iterator<String> iterator = attribute.getOptions().iterator(); iterator.hasNext(); ) {
            String option = iterator.next();
            if (StringUtils.isEmpty(option)) {
                iterator.remove();
            }
        }
        attribute.setProductCategory(productCategoryService.find(productCategoryId));
        if (!isValid(attribute, Save.class)) {
            return ERROR_VIEW;
        }
        if (attribute.getProductCategory().getAttributes().size() >= Product.ATTRIBUTE_VALUE_PROPERTY_COUNT) {
            addFlashMessage(redirectAttributes, Message.error("admin.attribute.addCountNotAllowed", Product.ATTRIBUTE_VALUE_PROPERTY_COUNT));
        } else {
            attribute.setPropertyIndex(null);
            attributeService.save(attribute);
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        }
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("attributeValuePropertyCount", Product.ATTRIBUTE_VALUE_PROPERTY_COUNT);
        model.addAttribute("attribute", attributeService.find(id));
        return "admin/attribute/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Attribute attribute, RedirectAttributes redirectAttributes) {
        for (Iterator<String> iterator = attribute.getOptions().iterator(); iterator.hasNext(); ) {
            String option = iterator.next();
            if (StringUtils.isEmpty(option)) {
                iterator.remove();
            }
        }
        if (!isValid(attribute)) {
            return ERROR_VIEW;
        }
        attributeService.update(attribute, "propertyIndex", "productCategory");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", attributeService.findPage(pageable));
        return "admin/attribute/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        attributeService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}