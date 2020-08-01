package com.x.shop.controller.admin;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPSenderFailedException;
import com.x.shop.common.CommonAttributes;
import com.x.shop.common.FileInfo.FileType;
import com.x.shop.common.Message;
import com.x.shop.common.Setting;
import com.x.shop.common.Setting.*;
import com.x.shop.service.CacheService;
import com.x.shop.service.FileService;
import com.x.shop.service.MailService;
import com.x.shop.service.StaticService;
import com.x.shop.util.SettingUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Controller - 系统设置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminSettingController")
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {

    @Resource(name = "fileServiceImpl")
    private FileService fileService;
    @Resource(name = "mailServiceImpl")
    private MailService mailService;
    @Resource(name = "cacheServiceImpl")
    private CacheService cacheService;
    @Resource(name = "staticServiceImpl")
    private StaticService staticService;

    /**
     * 邮件测试
     */
    @RequestMapping(value = "/mail_test", method = RequestMethod.POST)
    public @ResponseBody
    Message mailTest(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail) {
        if (StringUtils.isEmpty(toMail)) {
            return ERROR_MESSAGE;
        }
        Setting setting = SettingUtils.get();
        if (StringUtils.isEmpty(smtpPassword)) {
            smtpPassword = setting.getSmtpPassword();
        }
        try {
            if (!isValid(Setting.class, "smtpFromMail", smtpFromMail) || !isValid(Setting.class, "smtpHost", smtpHost) || !isValid(Setting.class, "smtpPort", smtpPort) || !isValid(Setting.class, "smtpUsername", smtpUsername)) {
                return ERROR_MESSAGE;
            }
            mailService.sendTestMail(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword, toMail);
        } catch (MailSendException e) {
            Exception[] messageExceptions = e.getMessageExceptions();
            if (messageExceptions != null) {
                for (Exception exception : messageExceptions) {
                    if (exception instanceof SMTPSendFailedException) {
                        SMTPSendFailedException smtpSendFailedException = (SMTPSendFailedException) exception;
                        Exception nextException = smtpSendFailedException.getNextException();
                        if (nextException instanceof SMTPSenderFailedException) {
                            return Message.error("admin.setting.mailTestSenderFailed");
                        }
                    } else if (exception instanceof MessagingException) {
                        MessagingException messagingException = (MessagingException) exception;
                        Exception nextException = messagingException.getNextException();
                        if (nextException instanceof UnknownHostException) {
                            return Message.error("admin.setting.mailTestUnknownHost");
                        } else if (nextException instanceof ConnectException) {
                            return Message.error("admin.setting.mailTestConnect");
                        }
                    }
                }
            }
            return Message.error("admin.setting.mailTestError");
        } catch (MailAuthenticationException e) {
            return Message.error("admin.setting.mailTestAuthentication");
        } catch (Exception e) {
            return Message.error("admin.setting.mailTestError");
        }
        return Message.success("admin.setting.mailTestSuccess");
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(ModelMap model) {
        model.addAttribute("watermarkPositions", WatermarkPosition.values());
        model.addAttribute("roundTypes", RoundType.values());
        model.addAttribute("captchaTypes", CaptchaType.values());
        model.addAttribute("accountLockTypes", AccountLockType.values());
        model.addAttribute("stockAllocationTimes", StockAllocationTime.values());
        model.addAttribute("reviewAuthorities", ReviewAuthority.values());
        model.addAttribute("consultationAuthorities", ConsultationAuthority.values());
        return "admin/setting/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Setting setting, MultipartFile watermarkImageFile, RedirectAttributes redirectAttributes) {
        if (!isValid(setting)) {
            return ERROR_VIEW;
        }
        if (setting.getUsernameMinLength() > setting.getUsernameMaxLength() || setting.getPasswordMinLength() > setting.getPasswordMaxLength()) {
            return ERROR_VIEW;
        }
        Setting srcSetting = SettingUtils.get();
        if (StringUtils.isEmpty(setting.getSmtpPassword())) {
            setting.setSmtpPassword(srcSetting.getSmtpPassword());
        }
        if (watermarkImageFile != null && !watermarkImageFile.isEmpty()) {
            if (!fileService.isValid(FileType.image, watermarkImageFile)) {
                addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
                return "redirect:edit.html";
            }
            String watermarkImage = fileService.uploadLocal(FileType.image, watermarkImageFile);
            setting.setWatermarkImage(watermarkImage);
        } else {
            setting.setWatermarkImage(srcSetting.getWatermarkImage());
        }
        setting.setCnzzSiteId(srcSetting.getCnzzSiteId());
        setting.setCnzzPassword(srcSetting.getCnzzPassword());
        SettingUtils.set(setting);
        cacheService.clear();
        staticService.buildIndex();
        staticService.buildOther();
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:edit.html";
    }

}