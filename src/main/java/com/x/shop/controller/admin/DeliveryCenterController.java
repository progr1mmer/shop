package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.DeliveryCenter;
import com.x.shop.service.AreaService;
import com.x.shop.service.DeliveryCenterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 发货点
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("deliveryCenterController")
@RequestMapping("/admin/delivery_center")
public class DeliveryCenterController extends BaseController {

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add() {
        return "admin/delivery_center/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(DeliveryCenter deliveryCenter, Long areaId, Model model, RedirectAttributes redirectAttributes) {
        deliveryCenter.setArea(areaService.find(areaId));
        if (!isValid(deliveryCenter)) {
            return ERROR_VIEW;
        }
        deliveryCenter.setAreaName(null);
        deliveryCenterService.save(deliveryCenter);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, Model model) {
        model.addAttribute("deliveryCenter", deliveryCenterService.find(id));
        return "admin/delivery_center/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(DeliveryCenter deliveryCenter, Long areaId, RedirectAttributes redirectAttributes) {
        deliveryCenter.setArea(areaService.find(areaId));
        if (!isValid(deliveryCenter)) {
            return ERROR_VIEW;
        }
        deliveryCenterService.update(deliveryCenter, "areaName");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model, Pageable pageable) {
        model.addAttribute("page", deliveryCenterService.findPage(pageable));
        return "admin/delivery_center/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        deliveryCenterService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}