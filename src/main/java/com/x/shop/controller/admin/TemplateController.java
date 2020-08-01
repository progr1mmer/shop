package com.x.shop.controller.admin;

import com.x.shop.common.Template.Type;
import com.x.shop.service.TemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;

/**
 * Controller - 模板
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminTemplateController")
@RequestMapping("/admin/template")
public class TemplateController extends BaseController {

    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(String id, ModelMap model) {
        if (StringUtils.isEmpty(id)) {
            return ERROR_VIEW;
        }
        model.addAttribute("template", templateService.get(id));
        model.addAttribute("content", templateService.read(id));
        return "admin/template/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(String id, String content, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(id) || content == null) {
            return ERROR_VIEW;
        }
        templateService.write(id, content);
        freeMarkerConfigurer.getConfiguration().clearTemplateCache();
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Type type, ModelMap model) {
        model.addAttribute("type", type);
        model.addAttribute("types", Type.values());
        model.addAttribute("templates", templateService.getList(type));
        return "admin/template/list";
    }

}