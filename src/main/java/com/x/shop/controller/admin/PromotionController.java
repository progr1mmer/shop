package com.x.shop.controller.admin;

import com.x.shop.common.Pageable;
import com.x.shop.entity.*;
import com.x.shop.service.*;
import com.x.shop.util.FreemarkerUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 促销
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminPromotionController")
@RequestMapping("/admin/promotion")
public class PromotionController extends BaseController {

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;
    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "brandServiceImpl")
    private BrandService brandService;
    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    /**
     * 检查价格运算表达式是否正确
     */
    @RequestMapping(value = "/check_price_expression", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkPriceExpression(String priceExpression) {
        if (StringUtils.isEmpty(priceExpression)) {
            return false;
        }
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("quantity", 111);
            model.put("price", new BigDecimal(9.99));
            new BigDecimal(FreemarkerUtils.process("#{(" + priceExpression + ");M50}", model));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查积分运算表达式是否正确
     */
    @RequestMapping(value = "/check_point_expression", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkPointExpression(String pointExpression) {
        if (StringUtils.isEmpty(pointExpression)) {
            return false;
        }
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("quantity", 111);
            model.put("point", 999L);
            Double.valueOf(FreemarkerUtils.process("#{(" + pointExpression + ");M50}", model)).longValue();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 商品选择
     */
    @RequestMapping(value = "/product_select", method = RequestMethod.GET)
    public @ResponseBody
    List<Map<String, Object>> productSelect(String q) {
        List<Map<String, Object>> data = new ArrayList<>();
        if (StringUtils.isNotEmpty(q)) {
            List<Product> products = productService.search(q, false, 20);
            for (Product product : products) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", product.getId());
                map.put("sn", product.getSn());
                map.put("fullName", product.getFullName());
                map.put("path", product.getPath());
                data.add(map);
            }
        }
        return data;
    }

    /**
     * 赠品选择
     */
    @RequestMapping(value = "/gift_select", method = RequestMethod.GET)
    public @ResponseBody
    List<Map<String, Object>> giftSelect(String q) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotEmpty(q)) {
            List<Product> products = productService.search(q, true, 20);
            for (Product product : products) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", product.getId());
                map.put("sn", product.getSn());
                map.put("fullName", product.getFullName());
                map.put("path", product.getPath());
                data.add(map);
            }
        }
        return data;
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("productCategories", productCategoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("coupons", couponService.findAll());
        return "admin/promotion/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Promotion promotion, Long[] memberRankIds, Long[] productCategoryIds, Long[] brandIds, Long[] couponIds, Long[] productIds, RedirectAttributes redirectAttributes) {
        promotion.setProductCategories(new HashSet<>(productCategoryService.findList(productCategoryIds)));
        promotion.setBrands(new HashSet<>(brandService.findList(brandIds)));
        promotion.setCoupons(new HashSet<>(couponService.findList(couponIds)));
        for (Product product : productService.findList(productIds)) {
            if (!product.getIsGift()) {
                promotion.getProducts().add(product);
            }
        }
        for (Iterator<GiftItem> iterator = promotion.getGiftItems().iterator(); iterator.hasNext(); ) {
            GiftItem giftItem = iterator.next();
            if (giftItem == null || giftItem.getGift() == null || giftItem.getGift().getId() == null) {
                iterator.remove();
            } else {
                giftItem.setGift(productService.find(giftItem.getGift().getId()));
                giftItem.setPromotion(promotion);
            }
        }
        if (!isValid(promotion)) {
            return ERROR_VIEW;
        }
        if (promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate())) {
            return ERROR_VIEW;
        }
        if (promotion.getMinimumQuantity() != null && promotion.getMaximumQuantity() != null && promotion.getMinimumQuantity() > promotion.getMaximumQuantity()) {
            return ERROR_VIEW;
        }
        if (promotion.getMinimumPrice() != null && promotion.getMaximumPrice() != null && promotion.getMinimumPrice().compareTo(promotion.getMaximumPrice()) > 0) {
            return ERROR_VIEW;
        }
        if (StringUtils.isNotEmpty(promotion.getPriceExpression())) {
            try {
                Map<String, Object> model = new HashMap<>();
                model.put("quantity", 111);
                model.put("price", new BigDecimal(9.99));
                new BigDecimal(FreemarkerUtils.process("#{(" + promotion.getPriceExpression() + ");M50}", model));
            } catch (Exception e) {
                return ERROR_VIEW;
            }
        }
        if (StringUtils.isNotEmpty(promotion.getPointExpression())) {
            try {
                Map<String, Object> model = new HashMap<>();
                model.put("quantity", 111);
                model.put("point", 999L);
                Double.valueOf(FreemarkerUtils.process("#{(" + promotion.getPointExpression() + ");M50}", model)).longValue();
            } catch (Exception e) {
                return ERROR_VIEW;
            }
        }
        promotionService.save(promotion);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("promotion", promotionService.find(id));
        model.addAttribute("productCategories", productCategoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("coupons", couponService.findAll());
        return "admin/promotion/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Promotion promotion, Long[] memberRankIds, Long[] productCategoryIds, Long[] brandIds, Long[] couponIds, Long[] productIds, RedirectAttributes redirectAttributes) {
        promotion.setProductCategories(new HashSet<ProductCategory>(productCategoryService.findList(productCategoryIds)));
        promotion.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
        promotion.setCoupons(new HashSet<Coupon>(couponService.findList(couponIds)));
        for (Product product : productService.findList(productIds)) {
            if (!product.getIsGift()) {
                promotion.getProducts().add(product);
            }
        }
        for (Iterator<GiftItem> iterator = promotion.getGiftItems().iterator(); iterator.hasNext(); ) {
            GiftItem giftItem = iterator.next();
            if (giftItem == null || giftItem.getGift() == null || giftItem.getGift().getId() == null) {
                iterator.remove();
            } else {
                giftItem.setGift(productService.find(giftItem.getGift().getId()));
                giftItem.setPromotion(promotion);
            }
        }
        if (promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate())) {
            return ERROR_VIEW;
        }
        if (promotion.getMinimumQuantity() != null && promotion.getMaximumQuantity() != null && promotion.getMinimumQuantity() > promotion.getMaximumQuantity()) {
            return ERROR_VIEW;
        }
        if (promotion.getMinimumPrice() != null && promotion.getMaximumPrice() != null && promotion.getMinimumPrice().compareTo(promotion.getMaximumPrice()) > 0) {
            return ERROR_VIEW;
        }
        if (StringUtils.isNotEmpty(promotion.getPriceExpression())) {
            try {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("quantity", 111);
                model.put("price", new BigDecimal(9.99));
                new BigDecimal(FreemarkerUtils.process("#{(" + promotion.getPriceExpression() + ");M50}", model));
            } catch (Exception e) {
                return ERROR_VIEW;
            }
        }
        if (StringUtils.isNotEmpty(promotion.getPointExpression())) {
            try {
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("quantity", 111);
                model.put("point", 999L);
                Double.valueOf(FreemarkerUtils.process("#{(" + promotion.getPointExpression() + ");M50}", model)).longValue();
            } catch (Exception e) {
                return ERROR_VIEW;
            }
        }
        promotionService.update(promotion);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", promotionService.findPage(pageable));
        return "admin/promotion/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    com.x.shop.common.Message delete(Long[] ids) {
        promotionService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}