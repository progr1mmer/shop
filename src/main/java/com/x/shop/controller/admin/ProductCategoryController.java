package com.x.shop.controller.admin;

import com.x.shop.common.Message;
import com.x.shop.entity.Brand;
import com.x.shop.entity.Product;
import com.x.shop.entity.ProductCategory;
import com.x.shop.service.BrandService;
import com.x.shop.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller - 商品分类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Controller("adminProductCategoryController")
@RequestMapping("/admin/product_category")
public class ProductCategoryController extends BaseController {

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;
    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        return "admin/product_category/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(ProductCategory productCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes) {
        productCategory.setParent(productCategoryService.find(parentId));
        productCategory.setBrands(new HashSet<>(brandService.findList(brandIds)));
        if (!isValid(productCategory)) {
            return ERROR_VIEW;
        }
        productCategory.setTreePath(null);
        productCategory.setGrade(null);
        productCategory.setChildren(null);
        productCategory.setProducts(null);
        productCategory.setParameterGroups(null);
        productCategory.setAttributes(null);
        productCategory.setPromotions(null);
        productCategoryService.save(productCategory);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        ProductCategory productCategory = productCategoryService.find(id);
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("children", productCategoryService.findChildren(productCategory));
        return "admin/product_category/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(ProductCategory productCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes) {
        productCategory.setParent(productCategoryService.find(parentId));
        productCategory.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
        if (!isValid(productCategory)) {
            return ERROR_VIEW;
        }
        if (productCategory.getParent() != null) {
            ProductCategory parent = productCategory.getParent();
            if (parent.equals(productCategory)) {
                return ERROR_VIEW;
            }
            List<ProductCategory> children = productCategoryService.findChildren(parent);
            if (children != null && children.contains(parent)) {
                return ERROR_VIEW;
            }
        }
        productCategoryService.update(productCategory, "treePath", "grade", "children", "products", "parameterGroups", "attributes", "promotions");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.html";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        return "admin/product_category/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long id) {
        ProductCategory productCategory = productCategoryService.find(id);
        if (productCategory == null) {
            return ERROR_MESSAGE;
        }
        Set<ProductCategory> children = productCategory.getChildren();
        if (children != null && !children.isEmpty()) {
            return Message.error("admin.productCategory.deleteExistChildrenNotAllowed");
        }
        Set<Product> products = productCategory.getProducts();
        if (products != null && !products.isEmpty()) {
            return Message.error("admin.productCategory.deleteExistProductNotAllowed");
        }
        productCategoryService.delete(id);
        return SUCCESS_MESSAGE;
    }

}