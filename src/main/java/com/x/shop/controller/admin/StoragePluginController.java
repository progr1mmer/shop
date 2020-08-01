package com.x.shop.controller.admin;

import com.x.shop.service.PluginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 存储插件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminStoragePluginController")
@RequestMapping("/admin/storage_plugin")
public class StoragePluginController extends BaseController {

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("storagePlugins", pluginService.getStoragePlugins());
        return "admin/storage_plugin/list";
    }

}