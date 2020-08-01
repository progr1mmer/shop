package com.x.shop.service;

import com.x.shop.entity.SafeKey;

import java.util.Map;

/**
 * Service - 邮件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface MailService {

    /**
     * 发送邮件
     *
     * @param smtpFromMail 发件人邮箱
     * @param smtpHost     SMTP服务器地址
     * @param smtpPort     SMTP服务器端口
     * @param smtpUsername SMTP用户名
     * @param smtpPassword SMTP密码
     * @param toMail       收件人邮箱
     * @param subject      主题
     * @param templatePath 模板路径
     * @param model        数据
     * @param async        是否异步
     */
    void send(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail, String subject, String templatePath, Map<String, Object> model, boolean async);

    /**
     * 发送邮件
     *
     * @param toMail       收件人邮箱
     * @param subject      主题
     * @param templatePath 模板路径
     * @param model        数据
     * @param async        是否异步
     */
    void send(String toMail, String subject, String templatePath, Map<String, Object> model, boolean async);

    /**
     * 发送邮件(异步)
     *
     * @param toMail       收件人邮箱
     * @param subject      主题
     * @param templatePath 模板路径
     * @param model        数据
     */
    void send(String toMail, String subject, String templatePath, Map<String, Object> model);

    /**
     * 发送邮件(异步)
     *
     * @param toMail       收件人邮箱
     * @param subject      主题
     * @param templatePath 模板路径
     */
    void send(String toMail, String subject, String templatePath);

    /**
     * 发送测试邮件
     *
     * @param smtpFromMail 发件人邮箱
     * @param smtpHost     SMTP服务器地址
     * @param smtpPort     SMTP服务器端口
     * @param smtpUsername SMTP用户名
     * @param smtpPassword SMTP密码
     * @param toMail       收件人邮箱
     */
    void sendTestMail(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail);

    /**
     * 发送找回密码邮件
     *
     * @param toMail   收件人邮箱
     * @param username 用户名
     * @param safeKey  安全密匙
     */
    void sendFindPasswordMail(String toMail, String username, SafeKey safeKey);

}