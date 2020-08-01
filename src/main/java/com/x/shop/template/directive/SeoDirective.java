package com.x.shop.template.directive;

import com.x.shop.entity.Seo;
import com.x.shop.entity.Seo.Type;
import com.x.shop.service.SeoService;
import com.x.shop.util.FreemarkerUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 模板指令 - SEO设置
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("seoDirective")
public class SeoDirective extends BaseDirective {

    /**
     * "类型"参数名称
     */
    private static final String TYPE_PARAMETER_NAME = "type";

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "seo";

    @Resource(name = "seoServiceImpl")
    private SeoService seoService;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Type type = FreemarkerUtils.getParameter(TYPE_PARAMETER_NAME, Type.class, params);

        Seo seo;
        boolean useCache = useCache(env, params);
        String cacheRegion = getCacheRegion(env, params);
        if (useCache) {
            seo = seoService.find(type, cacheRegion);
        } else {
            seo = seoService.find(type);
        }
        setLocalVariable(VARIABLE_NAME, seo, env, body);
    }

}