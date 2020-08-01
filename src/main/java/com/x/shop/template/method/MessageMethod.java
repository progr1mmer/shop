package com.x.shop.template.method;

import com.x.shop.util.SpringUtils;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模板方法 - 多语言
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("messageMethod")
public class MessageMethod implements TemplateMethodModelEx {

    @Override
    @SuppressWarnings("rawtypes")
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null && StringUtils.isNotEmpty(arguments.get(0).toString())) {
            String message;
            String code = arguments.get(0).toString();
            if (arguments.size() > 1) {
                Object[] args = arguments.subList(1, arguments.size()).toArray();
                message = SpringUtils.getMessage(code, args);
            } else {
                message = SpringUtils.getMessage(code);
            }
            return new SimpleScalar(message);
        }
        return null;
    }

}