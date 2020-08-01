package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Admin;
import com.x.shop.entity.BaseEntity.Save;
import com.x.shop.entity.Role;
import com.x.shop.service.AdminService;
import com.x.shop.service.RoleService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashSet;

/**
 * Controller - 管理员
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    /**
     * 检查用户名是否存在
     */
    @RequestMapping(value = "/check_username", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        if (adminService.usernameExists(username)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/admin/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
        admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
        if (!isValid(admin, Save.class)) {
            return ERROR_VIEW;
        }
        if (adminService.usernameExists(admin.getUsername())) {
            return ERROR_VIEW;
        }
        admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
        admin.setIsLocked(false);
        admin.setLoginFailureCount(0);
        admin.setLockedDate(null);
        admin.setLoginDate(null);
        admin.setLoginIp(null);
        admin.setOrders(null);
        adminService.save(admin);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("admin", adminService.find(id));
        return "admin/admin/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Admin admin, Long[] roleIds, RedirectAttributes redirectAttributes) {
        admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
        if (!isValid(admin)) {
            return ERROR_VIEW;
        }
        Admin pAdmin = adminService.find(admin.getId());
        if (pAdmin == null) {
            return ERROR_VIEW;
        }
        if (StringUtils.isNotEmpty(admin.getPassword())) {
            admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
        } else {
            admin.setPassword(pAdmin.getPassword());
        }
        if (pAdmin.getIsLocked() && !admin.getIsLocked()) {
            admin.setLoginFailureCount(0);
            admin.setLockedDate(null);
        } else {
            admin.setIsLocked(pAdmin.getIsLocked());
            admin.setLoginFailureCount(pAdmin.getLoginFailureCount());
            admin.setLockedDate(pAdmin.getLockedDate());
        }
        adminService.update(admin, "username", "loginDate", "loginIp", "orders");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", adminService.findPage(pageable));
        return "admin/admin/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids.length >= adminService.count()) {
            return Message.error("admin.common.deleteAllNotAllowed");
        }
        adminService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}