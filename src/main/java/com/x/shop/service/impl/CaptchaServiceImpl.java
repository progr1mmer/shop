package com.x.shop.service.impl;

import com.x.shop.common.Setting;
import com.x.shop.common.Setting.CaptchaType;
import com.x.shop.service.CaptchaService;
import com.x.shop.util.CaptchaUtils;
import com.x.shop.util.SettingUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * Service - 验证码
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements CaptchaService {

    @Override
    public BufferedImage buildImage(String captchaId) {
        return CaptchaUtils.createImage(captchaId);
    }

    @Override
    public boolean isValid(CaptchaType captchaType, String captchaId, String captcha) {
        Setting setting = SettingUtils.get();
        if (captchaType == null || ArrayUtils.contains(setting.getCaptchaTypes(), captchaType)) {
            if (StringUtils.isNotEmpty(captchaId) && StringUtils.isNotEmpty(captcha)) {
                try {
                    return CaptchaUtils.validateResponseForID(captchaId, captcha);
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}