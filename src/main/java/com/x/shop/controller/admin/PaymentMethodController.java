package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.PaymentMethod;
import com.x.shop.entity.ShippingMethod;
import com.x.shop.service.PaymentMethodService;
import com.x.shop.service.ShippingMethodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashSet;

/**
 * Controller - 支付方式
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminPaymentMethodController")
@RequestMapping("/admin/payment_method")
public class PaymentMethodController extends BaseController {

    @Resource(name = "paymentMethodServiceImpl")
    private PaymentMethodService paymentMethodService;
    @Resource(name = "shippingMethodServiceImpl")
    private ShippingMethodService shippingMethodService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("methods", PaymentMethod.Method.values());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        return "admin/payment_method/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(PaymentMethod paymentMethod, Long[] shippingMethodIds, RedirectAttributes redirectAttributes) {
        paymentMethod.setShippingMethods(new HashSet<ShippingMethod>(shippingMethodService.findList(shippingMethodIds)));
        if (!isValid(paymentMethod)) {
            return ERROR_VIEW;
        }
        paymentMethod.setOrders(null);
        paymentMethodService.save(paymentMethod);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("methods", PaymentMethod.Method.values());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("paymentMethod", paymentMethodService.find(id));
        return "admin/payment_method/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(PaymentMethod paymentMethod, Long[] shippingMethodIds, RedirectAttributes redirectAttributes) {
        paymentMethod.setShippingMethods(new HashSet<ShippingMethod>(shippingMethodService.findList(shippingMethodIds)));
        if (!isValid(paymentMethod)) {
            return ERROR_VIEW;
        }
        paymentMethodService.update(paymentMethod, "orders");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", paymentMethodService.findPage(pageable));
        return "admin/payment_method/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids.length >= paymentMethodService.count()) {
            return Message.error("admin.common.deleteAllNotAllowed");
        }
        paymentMethodService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}