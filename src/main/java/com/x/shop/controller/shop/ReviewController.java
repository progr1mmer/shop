package com.x.shop.controller.shop;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.common.ResourceNotFoundException;
import com.x.shop.common.Setting;
import com.x.shop.entity.Product;
import com.x.shop.entity.Review;
import com.x.shop.service.CaptchaService;
import com.x.shop.service.ProductService;
import com.x.shop.service.ReviewService;
import com.x.shop.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Controller - 评论
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopReviewController")
@RequestMapping("/review")
public class ReviewController extends BaseController {

    /**
     * 每页记录数
     */
    private static final int PAGE_SIZE = 10;

    @Resource(name = "reviewServiceImpl")
    private ReviewService reviewService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    /**
     * 发表
     */
    @RequestMapping(value = "/add/{id}.html", method = RequestMethod.GET)
    public String add(@PathVariable Long id, ModelMap model) {
        Setting setting = SettingUtils.get();
        if (!setting.getIsReviewEnabled()) {
            throw new ResourceNotFoundException();
        }
        Product product = productService.find(id);
        if (product == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("product", product);
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        return "/shop/review/add";
    }

    /**
     * 内容
     */
    @RequestMapping(value = "/content/{id}.html", method = RequestMethod.GET)
    public String content(@PathVariable Long id, Integer pageNumber, ModelMap model) {
        Setting setting = SettingUtils.get();
        if (!setting.getIsReviewEnabled()) {
            throw new ResourceNotFoundException();
        }
        Product product = productService.find(id);
        if (product == null) {
            throw new ResourceNotFoundException();
        }
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        model.addAttribute("product", product);
        model.addAttribute("page", reviewService.findPage(product, null, true, pageable));
        return "/shop/review/content";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody
    Message save(String captchaId, String captcha, Long id, Integer score, String content, HttpServletRequest request) {
        if (!captchaService.isValid(Setting.CaptchaType.review, captchaId, captcha)) {
            return Message.error("shop.captcha.invalid");
        }
        Setting setting = SettingUtils.get();
        if (!setting.getIsReviewEnabled()) {
            return Message.error("shop.review.disabled");
        }
        if (!isValid(Review.class, "score", score) || !isValid(Review.class, "content", content)) {
            return ERROR_MESSAGE;
        }
        Product product = productService.find(id);
        if (product == null) {
            return ERROR_MESSAGE;
        }
        /*Member member = memberService.getCurrent();
        if (setting.getReviewAuthority() != Setting.ReviewAuthority.anyone && member == null) {
            return Message.error("shop.review.accessDenied");
        }
        if (setting.getReviewAuthority() == Setting.ReviewAuthority.purchased) {
            if (!productService.isPurchased(member, product)) {
                return Message.error("shop.review.noPurchased");
            }
            if (reviewService.isReviewed(member, product)) {
                return Message.error("shop.review.reviewed");
            }
        }*/
        Review review = new Review();
        review.setScore(score);
        review.setContent(content);
        review.setIp(request.getRemoteAddr());
        //review.setMember(member);
        review.setProduct(product);
        if (setting.getIsReviewCheck()) {
            review.setIsShow(false);
            reviewService.save(review);
            return Message.success("shop.review.check");
        } else {
            review.setIsShow(true);
            reviewService.save(review);
            return Message.success("shop.review.success");
        }
    }

}