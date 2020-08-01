package com.x.shop.service;

import com.x.shop.common.Setting.CaptchaType;

import java.awt.image.BufferedImage;

/**
 * Service - 验证码
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface CaptchaService {

    /**
     * 生成验证码图片
     *
     * @param captchaId 验证ID
     * @return 验证码图片
     */
    BufferedImage buildImage(String captchaId);

    /**
     * 验证码验证
     *
     * @param captchaType 验证码类型
     * @param captchaId   验证ID
     * @param captcha     验证码(忽略大小写)
     * @return 验证码验证是否通过
     */
    boolean isValid(CaptchaType captchaType, String captchaId, String captcha);

}