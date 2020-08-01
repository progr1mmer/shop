package com.x.shop.controller.shop;

import com.x.shop.common.ResourceNotFoundException;
import com.x.shop.entity.Promotion;
import com.x.shop.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller - 促销
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopPromotionController")
@RequestMapping("/promotion")
public class PromotionController extends BaseController {

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    /**
     * 内容
     */
    @RequestMapping(value = "/content/{id}.html", method = RequestMethod.GET)
    public String content(@PathVariable Long id, ModelMap model) {
        Promotion promotion = promotionService.find(id);
        if (promotion == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("promotion", promotion);
        return "/shop/promotion/content";
    }

}