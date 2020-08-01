package com.x.shop.controller.admin;

import com.x.shop.common.Setting;
import com.x.shop.util.SettingUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Controller - 后台登陆
 *
 * @author progr1mmer.
 * @date Created on 2020/2/9.
 */
@Controller("adminLoginController")
public class LoginController extends BaseController {

    /**
     * 登录页面
     */
    @RequestMapping("/admin/login.html")
    public String index(ModelMap model, HttpServletRequest request) {
        String captchaId = UUID.randomUUID().toString();
        model.addAttribute("captchaId", captchaId);
        String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (loginFailure != null) {
            Setting setting = SettingUtils.get();
            String message = null;
            if ("org.apache.shiro.authc.pam.UnsupportedTokenException".equals(loginFailure)) {
                message = "admin.captcha.invalid";
            } else if ("org.apache.shiro.authc.UnknownAccountException".equals(loginFailure)) {
                message = "admin.login.unknownAccount";
            } else if ("org.apache.shiro.authc.DisabledAccountException".equals(loginFailure)) {
                message = "admin.login.disabledAccount";
            } else if ("org.apache.shiro.authc.LockedAccountException".equals(loginFailure)) {
                message = "admin.login.lockedAccount";
            } else if ("org.apache.shiro.authc.IncorrectCredentialsException".equals(loginFailure)) {
                if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.admin)) {
                    message = "admin.login.accountLockCount";
                } else {
                    message = "admin.login.incorrectCredentials";
                }
            } else if ("org.apache.shiro.authc.AuthenticationException".equals(loginFailure)) {
                message = "admin.login.authentication";
            }
            if (message != null) {
                model.addAttribute("shiroLoginFailure", message);
            }
        }
        return "admin/login";
    }

}
