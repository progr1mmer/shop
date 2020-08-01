package com.x.shop.controller.admin;

import com.x.shop.common.Pageable;
import com.x.shop.entity.Seo;
import com.x.shop.service.SeoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - SEO设置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminSeoController")
@RequestMapping("/admin/seo")
public class SeoController extends BaseController {

    @Resource(name = "seoServiceImpl")
    private SeoService seoService;

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("seo", seoService.find(id));
        return "admin/seo/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Seo seo, RedirectAttributes redirectAttributes) {
        if (!isValid(seo)) {
            return ERROR_VIEW;
        }
        seoService.update(seo, "type");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", seoService.findPage(pageable));
        return "admin/seo/list";
    }

}