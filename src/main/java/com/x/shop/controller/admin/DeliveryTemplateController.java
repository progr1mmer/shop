package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.DeliveryTemplate;
import com.x.shop.service.DeliveryTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 快递单模板
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminDeliveryTemplateController")
@RequestMapping("/admin/delivery_template")
public class DeliveryTemplateController extends BaseController {

    @Resource(name = "deliveryTemplateServiceImpl")
    private DeliveryTemplateService deliveryTemplateService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(Pageable pageable) {
        return "admin/delivery_template/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(DeliveryTemplate deliveryTemplate, RedirectAttributes redirectAttributes) {
        if (!isValid(deliveryTemplate)) {
            return ERROR_VIEW;
        }
        deliveryTemplateService.save(deliveryTemplate);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String eidt(Long id, Model model) {
        model.addAttribute("deliveryTemplate", deliveryTemplateService.find(id));
        return "admin/delivery_template/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String udpate(DeliveryTemplate deliveryTemplate, RedirectAttributes redirectAttributes) {
        if (!isValid(deliveryTemplate)) {
            return ERROR_VIEW;
        }
        deliveryTemplateService.update(deliveryTemplate);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, Model model) {
        model.addAttribute("page", deliveryTemplateService.findPage(pageable));
        return "admin/delivery_template/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        deliveryTemplateService.delete(ids);
        return SUCCESS_MESSAGE;
    }
}