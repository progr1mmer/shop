package com.x.shop.controller.admin;

import com.x.shop.entity.DeliveryCenter;
import com.x.shop.entity.DeliveryTemplate;
import com.x.shop.service.DeliveryCenterService;
import com.x.shop.service.DeliveryTemplateService;
import com.x.shop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 打印
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminPrintController")
@RequestMapping("/admin/print")
public class PrintController extends BaseController {

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;
    @Resource(name = "deliveryTemplateServiceImpl")
    private DeliveryTemplateService deliveryTemplateService;
    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    /**
     * 订单打印
     */
    @RequestMapping(value = "/order.html", method = RequestMethod.GET)
    public String order(Long id, ModelMap model) {
        model.addAttribute("order", orderService.find(id));
        return "admin/print/order";
    }

    /**
     * 购物单打印
     */
    @RequestMapping(value = "/product.html", method = RequestMethod.GET)
    public String product(Long id, ModelMap model) {
        model.addAttribute("order", orderService.find(id));
        return "admin/print/product";
    }

    /**
     * 配送单打印
     */
    @RequestMapping(value = "/shipping.html", method = RequestMethod.GET)
    public String shipping(Long id, ModelMap model) {
        model.addAttribute("order", orderService.find(id));
        return "admin/print/shipping";
    }

    /**
     * 快递单打印
     */
    @RequestMapping(value = "/delivery.html", method = RequestMethod.GET)
    public String delivery(Long orderId, Long deliveryTemplateId, Long deliveryCenterId, ModelMap model) {
        DeliveryTemplate deliveryTemplate = deliveryTemplateService.find(deliveryTemplateId);
        DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
        if (deliveryTemplate == null) {
            deliveryTemplate = deliveryTemplateService.findDefault();
        }
        if (deliveryCenter == null) {
            deliveryCenter = deliveryCenterService.findDefault();
        }
        model.addAttribute("deliveryTemplates", deliveryTemplateService.findAll());
        model.addAttribute("deliveryCenters", deliveryCenterService.findAll());
        model.addAttribute("order", orderService.find(orderId));
        model.addAttribute("deliveryTemplate", deliveryTemplate);
        model.addAttribute("deliveryCenter", deliveryCenter);
        return "admin/print/delivery";
    }

}