package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.common.Pageable;
import com.x.shop.entity.Role;
import com.x.shop.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Controller - 角色
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminRoleController")
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add() {
        return "admin/role/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Role role, RedirectAttributes redirectAttributes) {
        if (!isValid(role)) {
            return ERROR_VIEW;
        }
        role.setIsSystem(false);
        role.setAdmins(null);
        roleService.save(role);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("role", roleService.find(id));
        return "admin/role/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Role role, RedirectAttributes redirectAttributes) {
        if (!isValid(role)) {
            return ERROR_VIEW;
        }
        Role pRole = roleService.find(role.getId());
        if (pRole == null || pRole.getIsSystem()) {
            return ERROR_VIEW;
        }
        roleService.update(role, "isSystem", "admins");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", roleService.findPage(pageable));
        return "admin/role/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                Role role = roleService.find(id);
                if (role != null && (role.getIsSystem() || (role.getAdmins() != null && !role.getAdmins().isEmpty()))) {
                    return Message.error("admin.role.deleteExistNotAllowed", role.getName());
                }
            }
            roleService.delete(ids);
        }
        return SUCCESS_MESSAGE;
    }

}