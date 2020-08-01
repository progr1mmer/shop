package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.service.RefundsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Controller - 退款单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminRefundsController")
@RequestMapping("/admin/refunds")
public class RefundsController extends BaseController {

    @Resource(name = "refundsServiceImpl")
    private RefundsService refundsService;

    /**
     * 查看
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        model.addAttribute("refunds", refundsService.find(id));
        return "admin/refunds/view";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", refundsService.findPage(pageable));
        return "admin/refunds/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        refundsService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}