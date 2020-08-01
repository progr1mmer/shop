package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.FriendLink;
import com.x.shop.entity.FriendLink.Type;
import com.x.shop.service.FriendLinkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 友情链接
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminFriendLinkController")
@RequestMapping("/admin/friend_link")
public class FriendLinkController extends BaseController {

    @Resource(name = "friendLinkServiceImpl")
    private FriendLinkService friendLinkService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("types", Type.values());
        return "admin/friend_link/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(FriendLink friendLink, RedirectAttributes redirectAttributes) {
        if (!isValid(friendLink)) {
            return ERROR_VIEW;
        }
        if (friendLink.getType() == Type.text) {
            friendLink.setLogo(null);
        } else if (StringUtils.isEmpty(friendLink.getLogo())) {
            return ERROR_VIEW;
        }
        friendLinkService.save(friendLink);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("types", Type.values());
        model.addAttribute("friendLink", friendLinkService.find(id));
        return "admin/friend_link/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(FriendLink friendLink, RedirectAttributes redirectAttributes) {
        if (!isValid(friendLink)) {
            return ERROR_VIEW;
        }
        if (friendLink.getType() == Type.text) {
            friendLink.setLogo(null);
        } else if (StringUtils.isEmpty(friendLink.getLogo())) {
            return ERROR_VIEW;
        }
        friendLinkService.update(friendLink);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", friendLinkService.findPage(pageable));
        return "admin/friend_link/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        friendLinkService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}