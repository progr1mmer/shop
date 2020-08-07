package com.x.shop.controller.shop;

import com.x.shop.common.Message;
import com.x.shop.entity.Cart;
import com.x.shop.entity.CartItem;
import com.x.shop.entity.Product;
import com.x.shop.service.CartItemService;
import com.x.shop.service.CartService;
import com.x.shop.service.ProductService;
import com.x.shop.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Controller - 购物车
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("shopCartController")
@RequestMapping("/cart")
public class CartController extends BaseController {

    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "cartServiceImpl")
    private CartService cartService;
    @Resource(name = "cartItemServiceImpl")
    private CartItemService cartItemService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    Message add(Long id, Integer quantity, HttpServletRequest request, HttpServletResponse response) {
        if (quantity == null || quantity < 1) {
            return ERROR_MESSAGE;
        }
        Product product = productService.find(id);
        if (product == null) {
            return Message.warn("shop.cart.productNotExist");
        }
        if (!product.getIsMarketable()) {
            return Message.warn("shop.cart.productNotMarketable");
        }
        if (product.getIsGift()) {
            return Message.warn("shop.cart.notForSale");
        }

        Cart cart = cartService.getCurrent();

        if (cart == null) {
            cart = new Cart();
            cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
            cartService.save(cart);
        }

        if (cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
            return Message.warn("shop.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
        }

        if (cart.contains(product)) {
            CartItem cartItem = cart.getCartItem(product);
            if (cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
                return Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
            }
            if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
                return Message.warn("shop.cart.productLowStock");
            }
        } else {
            if (quantity > CartItem.MAX_QUANTITY) {
                return Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
            }
            if (product.getStock() != null && quantity > product.getAvailableStock()) {
                return Message.warn("shop.cart.productLowStock");
            }
            if (!cart.isEmpty()) {
                cart.getCartItems().forEach(cartItemService::delete);
                cart.setCartItems(new HashSet<>());
            }
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItemService.save(cartItem);
            cart.getCartItems().add(cartItem);
        }

        WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
        WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
        return Message.success("shop.cart.addSuccess", cart.getQuantity(), currency(cart.getEffectivePrice(), true, false));
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("cart", cartService.getCurrent());
        return "/shop/cart/list";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> edit(Long id, Integer quantity) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (quantity == null || quantity < 1) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            data.put("message", Message.error("shop.cart.notEmpty"));
            return data;
        }
        CartItem cartItem = cartItemService.find(id);
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
            data.put("message", Message.error("shop.cart.cartItemNotExist"));
            return data;
        }
        if (quantity > CartItem.MAX_QUANTITY) {
            data.put("message", Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY));
            return data;
        }
        Product product = cartItem.getProduct();
        if (product.getStock() != null && quantity > product.getAvailableStock()) {
            data.put("message", Message.warn("shop.cart.productLowStock"));
            return data;
        }
        cartItem.setQuantity(quantity);
        cartItemService.update(cartItem);

        data.put("message", SUCCESS_MESSAGE);
        data.put("subtotal", cartItem.getSubtotal());
        data.put("isLowStock", cartItem.getIsLowStock());
        data.put("quantity", cart.getQuantity());
        data.put("effectivePoint", cart.getEffectivePoint());
        data.put("effectivePrice", cart.getEffectivePrice());
        data.put("promotions", cart.getPromotions());
        data.put("giftItems", cart.getGiftItems());
        return data;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> delete(Long id) {
        Map<String, Object> data = new HashMap<String, Object>();
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            data.put("message", Message.error("shop.cart.notEmpty"));
            return data;
        }
        CartItem cartItem = cartItemService.find(id);
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
            data.put("message", Message.error("shop.cart.cartItemNotExist"));
            return data;
        }
        cartItems.remove(cartItem);
        cartItemService.delete(cartItem);

        data.put("message", SUCCESS_MESSAGE);
        data.put("quantity", cart.getQuantity());
        data.put("effectivePoint", cart.getEffectivePoint());
        data.put("effectivePrice", cart.getEffectivePrice());
        data.put("promotions", cart.getPromotions());
        data.put("isLowStock", cart.getIsLowStock());
        return data;
    }

    /**
     * 清空
     */
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public @ResponseBody
    Message clear() {
        Cart cart = cartService.getCurrent();
        cartService.delete(cart);
        return SUCCESS_MESSAGE;
    }

}