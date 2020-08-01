package com.x.shop.controller.shop;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.common.Setting;
import com.x.shop.entity.*;
import com.x.shop.plugin.PaymentPlugin;
import com.x.shop.service.*;
import com.x.shop.util.SettingUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 会员中心 - 订单
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopOrderController")
@RequestMapping("/order")
public class OrderController extends BaseController {

    /**
     * 每页记录数
     */
    private static final int PAGE_SIZE = 10;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;
    @Resource(name = "paymentMethodServiceImpl")
    private PaymentMethodService paymentMethodService;
    @Resource(name = "shippingMethodServiceImpl")
    private ShippingMethodService shippingMethodService;
    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;
    @Resource(name = "orderServiceImpl")
    private OrderService orderService;
    @Resource(name = "shippingServiceImpl")
    private ShippingService shippingService;
    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 订单锁定
     */
    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    public @ResponseBody
    boolean lock(String sn) {
        Order order = orderService.findBySn(sn);
        if (order != null && !order.isExpired() && !order.isLocked(null) && order.getPaymentMethod() != null && order.getPaymentMethod().getMethod() == PaymentMethod.Method.offline && (order.getPaymentStatus() == Order.PaymentStatus.unpaid || order.getPaymentStatus() == Order.PaymentStatus.partialPayment)) {
            order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
            order.setOperator(null);
            orderService.update(order);
            return true;
        }
        return false;
    }

    /**
     * 检查支付
     */
    @RequestMapping(value = "/check_payment", method = RequestMethod.POST)
    public @ResponseBody
    boolean checkPayment(String sn) {
        Order order = orderService.findBySn(sn);
        if (order != null && order.getPaymentStatus() == Order.PaymentStatus.paid) {
            return true;
        }
        return false;
    }

    /**
     * 优惠券信息
     */
    @RequestMapping(value = "/coupon_info", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> couponInfo(String code) {
        Map<String, Object> data = new HashMap<String, Object>();
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            data.put("message", Message.warn("shop.order.cartNotEmpty"));
            return data;
        }
        if (!cart.isCouponAllowed()) {
            data.put("message", Message.warn("shop.order.couponNotAllowed"));
            return data;
        }
        CouponCode couponCode = couponCodeService.findByCode(code);
        if (couponCode != null && couponCode.getCoupon() != null) {
            Coupon coupon = couponCode.getCoupon();
            if (!coupon.getIsEnabled()) {
                data.put("message", Message.warn("shop.order.couponDisabled"));
                return data;
            }
            if (!coupon.hasBegun()) {
                data.put("message", Message.warn("shop.order.couponNotBegin"));
                return data;
            }
            if (coupon.hasExpired()) {
                data.put("message", Message.warn("shop.order.couponHasExpired"));
                return data;
            }
            if (!cart.isValid(coupon)) {
                data.put("message", Message.warn("shop.order.couponInvalid"));
                return data;
            }
            if (couponCode.getIsUsed()) {
                data.put("message", Message.warn("shop.order.couponCodeUsed"));
                return data;
            }
            data.put("message", SUCCESS_MESSAGE);
            data.put("couponName", coupon.getName());
            return data;
        } else {
            data.put("message", Message.warn("shop.order.couponCodeNotExist"));
            return data;
        }
    }

    /**
     * 信息
     */
    @RequestMapping(value = "/info.html", method = RequestMethod.GET)
    public String info(ModelMap model) {
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart/list.html";
        }
        if (!isValid(cart)) {
            return ERROR_VIEW;
        }
        Order order = orderService.build(cart, null, null, null, null, null, null, null, null, false, null, false, null);
        model.addAttribute("order", order);
        model.addAttribute("cartToken", cart.getToken());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        return "/shop/order/info";
    }

    /**
     * 计算
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> calculate(Long paymentMethodId, Long shippingMethodId, String code, @RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle, @RequestParam(defaultValue = "false") Boolean useBalance, String memo) {
        Map<String, Object> data = new HashMap<String, Object>();
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            data.put("message", Message.error("shop.order.cartNotEmpty"));
            return data;
        }
        PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
        ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
        CouponCode couponCode = couponCodeService.findByCode(code);
        Order order = orderService.build(cart, null, null, null, null, null, paymentMethod, shippingMethod, couponCode, isInvoice, invoiceTitle, useBalance, memo);

        data.put("message", SUCCESS_MESSAGE);
        data.put("quantity", order.getQuantity());
        data.put("price", order.getPrice());
        data.put("freight", order.getFreight());
        data.put("promotionDiscount", order.getPromotionDiscount());
        data.put("couponDiscount", order.getCouponDiscount());
        data.put("tax", order.getTax());
        data.put("amountPayable", order.getAmountPayable());
        return data;
    }

    /**
     * 创建
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    Message create(String cartToken, String consignee, Long areaId, String address, String zipCode, String phone, Long paymentMethodId, Long shippingMethodId, String code, @RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle, @RequestParam(defaultValue = "false") Boolean useBalance, String memo) {
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            return Message.warn("shop.order.cartNotEmpty");
        }
        if (!StringUtils.equals(cart.getToken(), cartToken)) {
            return Message.warn("shop.order.cartHasChanged");
        }
        if (cart.getIsLowStock()) {
            return Message.warn("shop.order.cartLowStock");
        }
        PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
        if (paymentMethod == null) {
            return Message.error("shop.order.paymentMethodNotExsit");
        }
        ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
        if (shippingMethod == null) {
            return Message.error("shop.order.shippingMethodNotExsit");
        }
        if (!paymentMethod.getShippingMethods().contains(shippingMethod)) {
            return Message.error("shop.order.deliveryUnsupported");
        }
        CouponCode couponCode = couponCodeService.findByCode(code);
        Order order = orderService.create(cart, consignee, areaId, address, zipCode, phone, paymentMethod, shippingMethod, couponCode, isInvoice, invoiceTitle, useBalance, memo, null);
        return Message.success(order.getSn());
    }

    /**
     * 支付
     */
    @RequestMapping(value = "/payment.html", method = RequestMethod.GET)
    public String payment(String sn, ModelMap model) {
        Order order = orderService.findBySn(sn);
        if (order == null || order.isExpired() || order.getPaymentMethod() == null) {
            return ERROR_VIEW;
        }
        model.addAttribute("order", order);
        return "/shop/member/order/payment";
    }

    /**
     * 计算支付金额
     */
    @RequestMapping(value = "/calculate_amount", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> calculateAmount(String paymentPluginId, String sn) {
        Map<String, Object> data = new HashMap<>();
        Order order = orderService.findBySn(sn);
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (order == null || order.isExpired() || order.isLocked(null) || order.getPaymentMethod() == null || order.getPaymentMethod().getMethod() == PaymentMethod.Method.offline || paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        data.put("message", SUCCESS_MESSAGE);
        data.put("fee", paymentPlugin.calculateFee(order.getAmountPayable()));
        data.put("amount", paymentPlugin.calculateAmount(order.getAmountPayable()));
        return data;
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Integer pageNumber, ModelMap model) {
        //Member member = memberService.getCurrent();
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        model.addAttribute("page", orderService.findPage(pageable));
        return "shop/member/order/list";
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(String sn, ModelMap model) {
        Order order = orderService.findBySn(sn);
        if (order == null) {
            return ERROR_VIEW;
        }
        /*Member member = memberService.getCurrent();
        if (!member.getOrders().contains(order)) {
            return ERROR_VIEW;
        }*/
        model.addAttribute("order", order);
        return "shop/member/order/view";
    }

    /**
     * 取消
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public @ResponseBody
    Message cancel(String sn) {
        Order order = orderService.findBySn(sn);
        if (order != null && !order.isExpired() && order.getOrderStatus() == Order.OrderStatus.unconfirmed && order.getPaymentStatus() == Order.PaymentStatus.unpaid) {
            if (order.isLocked(null)) {
                return Message.warn("shop.member.order.locked");
            }
            orderService.cancel(order, null);
            return SUCCESS_MESSAGE;
        }
        return ERROR_MESSAGE;
    }

    /**
     * 物流动态
     */
    @RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> deliveryQuery(String sn) {
        Map<String, Object> data = new HashMap<String, Object>();
        Shipping shipping = shippingService.findBySn(sn);
        Setting setting = SettingUtils.get();
        if (shipping != null && shipping.getOrder() != null && StringUtils.isNotEmpty(setting.getKuaidi100Key()) && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()) && StringUtils.isNotEmpty(shipping.getTrackingNo())) {
            data = shippingService.query(shipping);
        }
        return data;
    }

}