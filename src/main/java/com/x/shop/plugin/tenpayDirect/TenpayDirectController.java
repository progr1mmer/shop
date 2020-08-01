package com.x.shop.plugin.tenpayDirect;

import com.x.shop.common.Message;
import com.x.shop.controller.admin.BaseController;
import com.x.shop.entity.PluginConfig;
import com.x.shop.plugin.PaymentPlugin;
import com.x.shop.service.PluginConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Controller - 财付通(即时交易)
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminTenpayDirectController")
@RequestMapping("/admin/payment_plugin/tenpay_direct")
public class TenpayDirectController extends BaseController {

    @Resource(name = "tenpayDirectPlugin")
    private TenpayDirectPlugin tenpayDirectPlugin;
    @Resource(name = "pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    /**
     * 安装
     */
    @RequestMapping(value = "/install", method = RequestMethod.POST)
    public @ResponseBody
    Message install() {
        if (!tenpayDirectPlugin.getIsInstalled()) {
            PluginConfig pluginConfig = new PluginConfig();
            pluginConfig.setPluginId(tenpayDirectPlugin.getId());
            pluginConfig.setIsEnabled(false);
            pluginConfigService.save(pluginConfig);
        }
        return SUCCESS_MESSAGE;
    }

    /**
     * 卸载
     */
    @RequestMapping(value = "/uninstall", method = RequestMethod.POST)
    public @ResponseBody
    Message uninstall() {
        if (tenpayDirectPlugin.getIsInstalled()) {
            PluginConfig pluginConfig = tenpayDirectPlugin.getPluginConfig();
            pluginConfigService.delete(pluginConfig);
        }
        return SUCCESS_MESSAGE;
    }

    /**
     * 设置
     */
    @RequestMapping(value = "/setting.html", method = RequestMethod.GET)
    public String setting(ModelMap model) {
        PluginConfig pluginConfig = tenpayDirectPlugin.getPluginConfig();
        model.addAttribute("feeTypes", PaymentPlugin.FeeType.values());
        model.addAttribute("pluginConfig", pluginConfig);
        return "/plugin/tenpayDirect/setting";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(String paymentName, String partner, String key, PaymentPlugin.FeeType feeType, BigDecimal fee, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
        PluginConfig pluginConfig = tenpayDirectPlugin.getPluginConfig();
        pluginConfig.setAttribute(PaymentPlugin.PAYMENT_NAME_ATTRIBUTE_NAME, paymentName);
        pluginConfig.setAttribute("partner", partner);
        pluginConfig.setAttribute("key", key);
        pluginConfig.setAttribute(PaymentPlugin.FEE_TYPE_ATTRIBUTE_NAME, feeType.toString());
        pluginConfig.setAttribute(PaymentPlugin.FEE_ATTRIBUTE_NAME, fee.toString());
        pluginConfig.setAttribute(PaymentPlugin.LOGO_ATTRIBUTE_NAME, logo);
        pluginConfig.setAttribute(PaymentPlugin.DESCRIPTION_ATTRIBUTE_NAME, description);
        pluginConfig.setIsEnabled(isEnabled);
        pluginConfig.setOrder(order);
        pluginConfigService.update(pluginConfig);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:/admin/payment_plugin/list.html";
    }

}