package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.BaseEntity.Save;
import com.x.shop.entity.Tag;
import com.x.shop.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 标签
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminTagController")
@RequestMapping("/admin/tag")
public class TagController extends BaseController {

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("types", Tag.Type.values());
        return "admin/tag/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Tag tag, RedirectAttributes redirectAttributes) {
        if (!isValid(tag, Save.class)) {
            return ERROR_VIEW;
        }
        tag.setArticles(null);
        tag.setProducts(null);
        tagService.save(tag);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("types", Tag.Type.values());
        model.addAttribute("tag", tagService.find(id));
        return "admin/tag/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Tag tag, RedirectAttributes redirectAttributes) {
        if (!isValid(tag)) {
            return ERROR_VIEW;
        }
        tagService.update(tag, "type", "articles", "products");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", tagService.findPage(pageable));
        return "admin/tag/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        tagService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}