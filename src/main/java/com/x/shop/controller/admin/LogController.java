package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Controller - 管理日志
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminLogController")
@RequestMapping("/admin/log")
public class LogController extends BaseController {

    @Resource(name = "logServiceImpl")
    private LogService logService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", logService.findPage(pageable));
        return "admin/log/list";
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        model.addAttribute("log", logService.find(id));
        return "admin/log/view";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        logService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    /**
     * 清空
     */
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public @ResponseBody
    Message clear() {
        logService.clear();
        return SUCCESS_MESSAGE;
    }

}