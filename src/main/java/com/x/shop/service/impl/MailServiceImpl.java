package com.x.shop.service.impl;

import com.x.shop.common.Setting;
import com.x.shop.common.Template;
import com.x.shop.entity.SafeKey;
import com.x.shop.service.MailService;
import com.x.shop.service.TemplateService;
import com.x.shop.util.SettingUtils;
import com.x.shop.util.SpringUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service - 邮件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("mailServiceImpl")
public class MailServiceImpl implements MailService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Autowired
    private TaskExecutor taskExecutor;
    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    /**
     * 添加邮件发送任务
     *
     * @param mimeMessage MimeMessage
     */
    private void addSendTask(final MimeMessage mimeMessage) {
        try {
            taskExecutor.execute(() -> javaMailSender.send(mimeMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail, String subject, String templatePath, Map<String, Object> model, boolean async) {
        Assert.hasText(smtpFromMail);
        Assert.hasText(smtpHost);
        Assert.notNull(smtpPort);
        Assert.hasText(smtpUsername);
        Assert.hasText(smtpPassword);
        Assert.hasText(toMail);
        Assert.hasText(subject);
        Assert.hasText(templatePath);
        try {
            Setting setting = SettingUtils.get();
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            freemarker.template.Template template = configuration.getTemplate(templatePath);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            javaMailSender.setHost(smtpHost);
            javaMailSender.setPort(smtpPort);
            javaMailSender.setUsername(smtpUsername);
            javaMailSender.setPassword(smtpPassword);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessageHelper.setFrom(MimeUtility.encodeWord(setting.getSiteName()) + " <" + smtpFromMail + ">");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(toMail);
            mimeMessageHelper.setText(text, true);
            if (async) {
                addSendTask(mimeMessage);
            } else {
                javaMailSender.send(mimeMessage);
            }
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String toMail, String subject, String templatePath, Map<String, Object> model, boolean async) {
        Setting setting = SettingUtils.get();
        send(setting.getSmtpFromMail(), setting.getSmtpHost(), setting.getSmtpPort(), setting.getSmtpUsername(), setting.getSmtpPassword(), toMail, subject, templatePath, model, async);
    }

    @Override
    public void send(String toMail, String subject, String templatePath, Map<String, Object> model) {
        Setting setting = SettingUtils.get();
        send(setting.getSmtpFromMail(), setting.getSmtpHost(), setting.getSmtpPort(), setting.getSmtpUsername(), setting.getSmtpPassword(), toMail, subject, templatePath, model, true);
    }

    @Override
    public void send(String toMail, String subject, String templatePath) {
        Setting setting = SettingUtils.get();
        send(setting.getSmtpFromMail(), setting.getSmtpHost(), setting.getSmtpPort(), setting.getSmtpUsername(), setting.getSmtpPassword(), toMail, subject, templatePath, null, true);
    }

    @Override
    public void sendTestMail(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail) {
        Setting setting = SettingUtils.get();
        String subject = SpringUtils.getMessage("admin.setting.testMailSubject", setting.getSiteName());
        Template testMailTemplate = templateService.get("testMail");
        send(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword, toMail, subject, testMailTemplate.getTemplatePath(), null, false);
    }

    @Override
    public void sendFindPasswordMail(String toMail, String username, SafeKey safeKey) {
        Setting setting = SettingUtils.get();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("username", username);
        model.put("safeKey", safeKey);
        String subject = SpringUtils.getMessage("shop.password.mailSubject", setting.getSiteName());
        Template findPasswordMailTemplate = templateService.get("findPasswordMail");
        send(toMail, subject, findPasswordMailTemplate.getTemplatePath(), model);
    }

}