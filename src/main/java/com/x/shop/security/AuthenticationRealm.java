package com.x.shop.security;

import com.x.shop.common.Principal;
import com.x.shop.common.Setting;
import com.x.shop.entity.Admin;
import com.x.shop.service.AdminService;
import com.x.shop.service.CaptchaService;
import com.x.shop.util.SettingUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Date;
import java.util.List;

/**
 * 权限认证
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public class AuthenticationRealm extends AuthorizingRealm {

    private CaptchaService captchaService;
    private AdminService adminService;

    AuthenticationRealm(CaptchaService captchaService, AdminService adminService) {
        this.captchaService = captchaService;
        this.adminService = adminService;
    }

    /**
     * 获取认证信息
     *
     * @param token 令牌
     * @return 认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken token) {
        AuthenticationToken authenticationToken = (AuthenticationToken) token;
        String username = authenticationToken.getUsername();
        String password = authenticationToken.getPassword() != null ? new String(authenticationToken.getPassword()) : "";
        String captchaId = authenticationToken.getCaptchaId();
        String captcha = authenticationToken.getCaptcha();
        String ip = authenticationToken.getHost();
        if (!captchaService.isValid(Setting.CaptchaType.adminLogin, captchaId, captcha)) {
            throw new UnsupportedTokenException();
        }
        if (username != null) {
            Admin admin = adminService.findByUsername(username);
            if (admin == null) {
                throw new UnknownAccountException();
            }
            if (!admin.getIsEnabled()) {
                throw new DisabledAccountException();
            }
            Setting setting = SettingUtils.get();
            if (admin.getIsLocked()) {
                if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.admin)) {
                    int loginFailureLockTime = setting.getAccountLockTime();
                    if (loginFailureLockTime == 0) {
                        throw new LockedAccountException();
                    }
                    Date lockedDate = admin.getLockedDate();
                    Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
                    if (new Date().after(unlockDate)) {
                        admin.setLoginFailureCount(0);
                        admin.setIsLocked(false);
                        admin.setLockedDate(null);
                        adminService.update(admin);
                    } else {
                        throw new LockedAccountException();
                    }
                } else {
                    admin.setLoginFailureCount(0);
                    admin.setIsLocked(false);
                    admin.setLockedDate(null);
                    adminService.update(admin);
                }
            }
            if (!DigestUtils.md5Hex(password).equals(admin.getPassword())) {
                int loginFailureCount = admin.getLoginFailureCount() + 1;
                if (loginFailureCount >= setting.getAccountLockCount()) {
                    admin.setIsLocked(true);
                    admin.setLockedDate(new Date());
                }
                admin.setLoginFailureCount(loginFailureCount);
                adminService.update(admin);
                throw new IncorrectCredentialsException();
            }
            admin.setLoginIp(ip);
            admin.setLoginDate(new Date());
            admin.setLoginFailureCount(0);
            adminService.update(admin);
            return new SimpleAuthenticationInfo(new Principal(admin.getId(), username), password, getName());
        }
        throw new UnknownAccountException();
    }

    /**
     * 获取授权信息
     *
     * @param principals principals
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) principals.fromRealm(getName()).iterator().next();
        if (principal != null) {
            List<String> authorities = adminService.findAuthorities(principal.getId());
            if (authorities != null) {
                SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
                authorizationInfo.addStringPermissions(authorities);
                return authorizationInfo;
            }
        }
        return null;
    }

}