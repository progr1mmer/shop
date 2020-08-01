package com.x.shop.template.method;

import com.x.shop.common.Setting;
import com.x.shop.util.SettingUtils;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 模板方法 - 货币格式化
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("currencyMethod")
public class CurrencyMethod implements TemplateMethodModelEx {

    @Override
    @SuppressWarnings("rawtypes")
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null && StringUtils.isNotEmpty(arguments.get(0).toString())) {
            boolean showSign = false;
            boolean showUnit = false;
            if (arguments.size() == 2) {
                if (arguments.get(1) != null) {
                    showSign = Boolean.valueOf(arguments.get(1).toString());
                }
            } else if (arguments.size() > 2) {
                if (arguments.get(1) != null) {
                    showSign = Boolean.valueOf(arguments.get(1).toString());
                }
                if (arguments.get(2) != null) {
                    showUnit = Boolean.valueOf(arguments.get(2).toString());
                }
            }
            Setting setting = SettingUtils.get();
            BigDecimal amount = new BigDecimal(arguments.get(0).toString());
            String price = setting.setScale(amount).toString();
            if (showSign) {
                price = setting.getCurrencySign() + price;
            }
            if (showUnit) {
                price += setting.getCurrencyUnit();
            }
            return new SimpleScalar(price);
        }
        return null;
    }

}