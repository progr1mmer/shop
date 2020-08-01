package com.x.shop.template.directive;

import com.x.shop.interceptor.ExecuteTimeInterceptor;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Map;

/**
 * 模板指令 - 执行时间
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("executeTimeDirective")
public class ExecuteTimeDirective extends BaseDirective {

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "executeTime";

    @Override
    @SuppressWarnings("rawtypes")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Long executeTime = (Long) requestAttributes.getAttribute(ExecuteTimeInterceptor.EXECUTE_TIME_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
        if (executeTime != null) {
            setLocalVariable(VARIABLE_NAME, executeTime, env, body);
        }
    }

}