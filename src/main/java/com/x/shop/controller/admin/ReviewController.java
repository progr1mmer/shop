package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Review;
import com.x.shop.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 评论
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminReviewController")
@RequestMapping("/admin/review")
public class ReviewController extends BaseController {

    @Resource(name = "reviewServiceImpl")
    private ReviewService reviewService;

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("review", reviewService.find(id));
        return "admin/review/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long id, @RequestParam(defaultValue = "false") Boolean isShow, RedirectAttributes redirectAttributes) {
        Review review = reviewService.find(id);
        if (review == null) {
            return ERROR_VIEW;
        }
        review.setIsShow(isShow);
        reviewService.update(review);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Review.Type type, Pageable pageable, ModelMap model) {
        model.addAttribute("type", type);
        model.addAttribute("types", Review.Type.values());
        model.addAttribute("page", reviewService.findPage(null, type, null, pageable));
        return "admin/review/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        reviewService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}