package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.service.ReturnsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Controller - 退货单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminReturnsController")
@RequestMapping("/admin/returns")
public class ReturnsController extends BaseController {

    @Resource(name = "returnsServiceImpl")
    private ReturnsService returnsService;

    /**
     * 查看
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        model.addAttribute("returns", returnsService.find(id));
        return "admin/returns/view";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", returnsService.findPage(pageable));
        return "admin/returns/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        returnsService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}