package com.x.shop.service.impl;

import com.x.shop.common.Template;
import com.x.shop.dao.ArticleDao;
import com.x.shop.dao.BrandDao;
import com.x.shop.dao.ProductDao;
import com.x.shop.dao.PromotionDao;
import com.x.shop.entity.Article;
import com.x.shop.entity.Brand;
import com.x.shop.entity.Product;
import com.x.shop.entity.Promotion;
import com.x.shop.service.StaticService;
import com.x.shop.service.TemplateService;
import com.x.shop.util.ContextPathUtils;
import com.x.shop.util.FreemarkerUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 静态化
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("staticServiceImpl")
public class StaticServiceImpl implements StaticService {

    /**
     * Sitemap最大地址数
     */
    private static final Integer SITEMAP_MAX_SIZE = 40000;

    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;
    @Resource(name = "articleDaoImpl")
    private ArticleDao articleDao;
    @Resource(name = "productDaoImpl")
    private ProductDao productDao;
    @Resource(name = "brandDaoImpl")
    private BrandDao brandDao;
    @Resource(name = "promotionDaoImpl")
    private PromotionDao promotionDao;

    @Override
    @Transactional(readOnly = true)
    public int build(String templatePath, String staticPath, Map<String, Object> model) {
        Assert.hasText(templatePath);
        Assert.hasText(staticPath);

        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        Writer writer = null;
        try {
            freemarker.template.Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
            File staticFile = new File(ContextPathUtils.getStaticPath(staticPath));
            File staticDirectory = staticFile.getParentFile();
            if (!staticDirectory.exists()) {
                staticDirectory.mkdirs();
            }
            fileOutputStream = new FileOutputStream(staticFile);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            writer = new BufferedWriter(outputStreamWriter);
            template.process(model, writer);
            writer.flush();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(outputStreamWriter);
            IOUtils.closeQuietly(fileOutputStream);
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public int build(String templatePath, String staticPath) {
        return build(templatePath, staticPath, null);
    }

    @Override
    @Transactional(readOnly = true)
    public int build(Article article) {
        Assert.notNull(article);

        delete(article);
        Template template = templateService.get("articleContent");
        int buildCount = 0;
        if (article.getIsPublication()) {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("article", article);
            for (int pageNumber = 1; pageNumber <= article.getTotalPages(); pageNumber++) {
                article.setPageNumber(pageNumber);
                buildCount += build(template.getTemplatePath(), article.getPath(), model);
            }
            article.setPageNumber(null);
        }
        return buildCount;
    }

    @Override
    @Transactional(readOnly = true)
    public int build(Product product) {
        Assert.notNull(product);

        delete(product);
        Template template = templateService.get("productContent");
        int buildCount = 0;
        if (product.getIsMarketable()) {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("product", product);
            buildCount += build(template.getTemplatePath(), product.getPath(), model);
        }
        return buildCount;
    }

    @Override
    @Transactional(readOnly = true)
    public int buildIndex() {
        Template template = templateService.get("index");
        return build(template.getTemplatePath(), template.getStaticPath());
    }

    @Override
    @Transactional(readOnly = true)
    public int buildSitemap() {
        int buildCount = 0;
        Template sitemapIndexTemplate = templateService.get("sitemapIndex");
        Template sitemapTemplate = templateService.get("sitemap");
        Map<String, Object> model = new HashMap<String, Object>();
        List<String> staticPaths = new ArrayList<String>();
        for (int step = 0, index = 0, first = 0, count = SITEMAP_MAX_SIZE; ; ) {
            try {
                model.put("index", index);
                String templatePath = sitemapTemplate.getTemplatePath();
                String staticPath = FreemarkerUtils.process(sitemapTemplate.getStaticPath(), model);
                if (step == 0) {
                    List<Article> articles = articleDao.findList(first, count, new ArrayList<>(), new ArrayList<>());
                    model.put("articles", articles);
                    if (articles.size() < count) {
                        step++;
                        first = 0;
                        count -= articles.size();
                    } else {
                        buildCount += build(templatePath, staticPath, model);
                        articleDao.clear();
                        articleDao.flush();
                        staticPaths.add(staticPath);
                        model.clear();
                        index++;
                        first += articles.size();
                        count = SITEMAP_MAX_SIZE;
                    }
                } else if (step == 1) {
                    List<Product> products = productDao.findList(first, count, new ArrayList<>(), new ArrayList<>());
                    model.put("products", products);
                    if (products.size() < count) {
                        step++;
                        first = 0;
                        count -= products.size();
                    } else {
                        buildCount += build(templatePath, staticPath, model);
                        productDao.clear();
                        productDao.flush();
                        staticPaths.add(staticPath);
                        model.clear();
                        index++;
                        first += products.size();
                        count = SITEMAP_MAX_SIZE;
                    }
                } else if (step == 2) {
                    List<Brand> brands = brandDao.findList(first, count, new ArrayList<>(), new ArrayList<>());
                    model.put("brands", brands);
                    if (brands.size() < count) {
                        step++;
                        first = 0;
                        count -= brands.size();
                    } else {
                        buildCount += build(templatePath, staticPath, model);
                        brandDao.clear();
                        brandDao.flush();
                        staticPaths.add(staticPath);
                        model.clear();
                        index++;
                        first += brands.size();
                        count = SITEMAP_MAX_SIZE;
                    }
                } else if (step == 3) {
                    List<Promotion> promotions = promotionDao.findList(first, count, new ArrayList<>(), new ArrayList<>());
                    model.put("promotions", promotions);
                    buildCount += build(templatePath, staticPath, model);
                    promotionDao.clear();
                    promotionDao.flush();
                    staticPaths.add(staticPath);
                    if (promotions.size() < count) {
                        model.put("staticPaths", staticPaths);
                        buildCount += build(sitemapIndexTemplate.getTemplatePath(), sitemapIndexTemplate.getStaticPath(), model);
                        break;
                    } else {
                        model.clear();
                        index++;
                        first += promotions.size();
                        count = SITEMAP_MAX_SIZE;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buildCount;
    }

    @Override
    @Transactional(readOnly = true)
    public int buildOther() {
        int buildCount = 0;
        Template shopCommonJsTemplate = templateService.get("shopCommonJs");
        Template adminCommonJsTemplate = templateService.get("adminCommonJs");
        buildCount += build(shopCommonJsTemplate.getTemplatePath(), shopCommonJsTemplate.getStaticPath());
        buildCount += build(adminCommonJsTemplate.getTemplatePath(), adminCommonJsTemplate.getStaticPath());
        return buildCount;
    }

    @Override
    @Transactional(readOnly = true)
    public int buildAll() {
        int buildCount = 0;
        for (int i = 0; i < articleDao.count(); i += 20) {
            List<Article> articles = articleDao.findList(i, 20, new ArrayList<>(), new ArrayList<>());
            for (Article article : articles) {
                buildCount += build(article);
            }
            articleDao.clear();
        }
        for (int i = 0; i < productDao.count(); i += 20) {
            List<Product> products = productDao.findList(i, 20, new ArrayList<>(), new ArrayList<>());
            for (Product product : products) {
                buildCount += build(product);
            }
            productDao.clear();
        }
        buildIndex();
        buildSitemap();
        buildOther();
        return buildCount;
    }

    @Override
    @Transactional(readOnly = true)
    public int delete(String staticPath) {
        Assert.hasText(staticPath);
        File staticFile = new File(ContextPathUtils.getStaticPath(staticPath));
        if (staticFile.exists()) {
            staticFile.delete();
            return 1;
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public int delete(Article article) {
        Assert.notNull(article);

        int deleteCount = 0;
        for (int pageNumber = 1; pageNumber <= article.getTotalPages() + 1000; pageNumber++) {
            article.setPageNumber(pageNumber);
            int count = delete(article.getPath());
            if (count < 1) {
                break;
            }
            deleteCount += count;
        }
        article.setPageNumber(null);
        return deleteCount;
    }

    @Override
    @Transactional(readOnly = true)
    public int delete(Product product) {
        Assert.notNull(product);

        return delete(product.getPath());
    }

    @Override
    @Transactional(readOnly = true)
    public int deleteIndex() {
        Template template = templateService.get("index");
        return delete(template.getStaticPath());
    }

    @Override
    @Transactional(readOnly = true)
    public int deleteOther() {
        int deleteCount = 0;
        Template shopCommonJsTemplate = templateService.get("shopCommonJs");
        Template adminCommonJsTemplate = templateService.get("adminCommonJs");
        deleteCount += delete(shopCommonJsTemplate.getStaticPath());
        deleteCount += delete(adminCommonJsTemplate.getStaticPath());
        return deleteCount;
    }

}