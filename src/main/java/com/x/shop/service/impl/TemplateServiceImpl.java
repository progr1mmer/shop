package com.x.shop.service.impl;

import com.x.shop.common.CommonAttributes;
import com.x.shop.common.Template;
import com.x.shop.service.TemplateService;
import com.x.shop.util.ContextPathUtils;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service - 模板
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("templateServiceImpl")
public class TemplateServiceImpl implements TemplateService {

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable("template")
    public List<Template> getAll() {
        try {
            File shopXmlFile = new ClassPathResource(CommonAttributes.SHOP_XML_PATH).getFile();
            Document document = new SAXReader().read(shopXmlFile);
            List<Template> templates = new ArrayList<>();
            List<Element> elements = document.selectNodes("/shop/template");
            for (Element element : elements) {
                Template template = getTemplate(element);
                templates.add(template);
            }
            return templates;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable("template")
    public List<Template> getList(Template.Type type) {
        if (type != null) {
            try {
                File shopXmlFile = new ClassPathResource(CommonAttributes.SHOP_XML_PATH).getFile();
                Document document = new SAXReader().read(shopXmlFile);
                List<Template> templates = new ArrayList<Template>();
                List<Element> elements = document.selectNodes("/shop/template[@type='" + type + "']");
                for (Element element : elements) {
                    Template template = getTemplate(element);
                    templates.add(template);
                }
                return templates;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return getAll();
        }
    }

    @Override
    @Cacheable("template")
    public Template get(String id) {
        try {
            File shopXmlFile = new ClassPathResource(CommonAttributes.SHOP_XML_PATH).getFile();
            Document document = new SAXReader().read(shopXmlFile);
            Element element = (Element) document.selectSingleNode("/shop/template[@id='" + id + "']");
            Template template = getTemplate(element);
            return template;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String read(String id) {
        Template template = get(id);
        return read(template);
    }

    @Override
    public String read(Template template) {
        String templateContent = null;
        try {
            File templateFile = new ClassPathResource("templates" + template.getTemplatePath()).getFile();
            templateContent = FileUtils.readFileToString(templateFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateContent;
    }

    @Override
    public void write(String id, String content) {
        Template template = get(id);
        write(template, content);
    }

    @Override
    public void write(Template template, String content) {
        try {
            File templateFile = new ClassPathResource("templates" + template.getTemplatePath()).getFile();
            FileUtils.writeStringToFile(templateFile, content, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取模板
     *
     * @param element 元素
     */
    private Template getTemplate(Element element) {
        String id = element.attributeValue("id");
        String type = element.attributeValue("type");
        String name = element.attributeValue("name");
        String templatePath = element.attributeValue("templatePath");
        String staticPath = element.attributeValue("staticPath");
        String description = element.attributeValue("description");

        Template template = new Template();
        template.setId(id);
        template.setType(Template.Type.valueOf(type));
        template.setName(name);
        template.setTemplatePath(templatePath);
        template.setStaticPath(staticPath);
        template.setDescription(description);
        return template;
    }

}