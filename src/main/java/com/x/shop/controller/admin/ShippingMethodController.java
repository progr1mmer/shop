package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.ShippingMethod;
import com.x.shop.service.DeliveryCorpService;
import com.x.shop.service.ShippingMethodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 配送方式
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminShippingMethodController")
@RequestMapping("/admin/shipping_method")
public class ShippingMethodController extends BaseController {

    @Resource(name = "shippingMethodServiceImpl")
    private ShippingMethodService shippingMethodService;
    @Resource(name = "deliveryCorpServiceImpl")
    private DeliveryCorpService deliveryCorpService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        return "admin/shipping_method/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(ShippingMethod shippingMethod, Long defaultDeliveryCorpId, RedirectAttributes redirectAttributes) {
        shippingMethod.setDefaultDeliveryCorp(deliveryCorpService.find(defaultDeliveryCorpId));
        if (!isValid(shippingMethod)) {
            return ERROR_VIEW;
        }
        shippingMethod.setPaymentMethods(null);
        shippingMethod.setOrders(null);
        shippingMethodService.save(shippingMethod);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        model.addAttribute("shippingMethod", shippingMethodService.find(id));
        return "admin/shipping_method/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(ShippingMethod shippingMethod, Long defaultDeliveryCorpId, RedirectAttributes redirectAttributes) {
        shippingMethod.setDefaultDeliveryCorp(deliveryCorpService.find(defaultDeliveryCorpId));
        if (!isValid(shippingMethod)) {
            return ERROR_VIEW;
        }
        shippingMethodService.update(shippingMethod, "paymentMethods", "orders");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", shippingMethodService.findPage(pageable));
        return "admin/shipping_method/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids.length >= shippingMethodService.count()) {
            return Message.error("admin.common.deleteAllNotAllowed");
        }
        shippingMethodService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}