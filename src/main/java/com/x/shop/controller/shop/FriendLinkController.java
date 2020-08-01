package com.x.shop.controller.shop;

import com.x.shop.entity.FriendLink.Type;
import com.x.shop.service.FriendLinkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 友情链接
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopFriendLinkController")
@RequestMapping("/friend_link")
public class FriendLinkController extends BaseController {

    @Resource(name = "friendLinkServiceImpl")
    private FriendLinkService friendLinkService;

    /**
     * 首页
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("textFriendLinks", friendLinkService.findList(Type.text));
        model.addAttribute("imageFriendLinks", friendLinkService.findList(Type.image));
        return "/shop/friend_link/index";
    }

}