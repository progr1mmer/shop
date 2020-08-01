package com.x.shop.controller.admin;

import com.x.shop.service.PluginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 支付插件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminPaymentPluginController")
@RequestMapping("/admin/payment_plugin")
public class PaymentPluginController extends BaseController {

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("paymentPlugins", pluginService.getPaymentPlugins());
        return "admin/payment_plugin/list";
    }

}