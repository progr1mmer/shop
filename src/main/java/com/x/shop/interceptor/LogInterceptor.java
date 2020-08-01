package com.x.shop.interceptor;

import com.x.shop.common.LogConfig;
import com.x.shop.entity.Log;
import com.x.shop.service.AdminService;
import com.x.shop.service.LogConfigService;
import com.x.shop.service.LogService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Interceptor - 日志记录
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 默认忽略参数
     */
    private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[]{"password", "rePassword", "currentPassword"};

    /**
     * antPathMatcher
     */
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 忽略参数
     */
    private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

    @Resource(name = "logConfigServiceImpl")
    private LogConfigService logConfigService;
    @Resource(name = "logServiceImpl")
    private LogService logService;
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @SuppressWarnings("unchecked")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        List<LogConfig> logConfigs = logConfigService.getAll();
        if (logConfigs != null) {
            String path = request.getServletPath();
            for (LogConfig logConfig : logConfigs) {
                if (antPathMatcher.match(logConfig.getUrlPattern(), path)) {
                    String username = adminService.getCurrentUsername();
                    String operation = logConfig.getOperation();
                    String operator = username;
                    String content = (String) request.getAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
                    String ip = request.getRemoteAddr();
                    request.removeAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
                    StringBuilder parameter = new StringBuilder();
                    Map<String, String[]> parameterMap = request.getParameterMap();
                    if (parameterMap != null) {
                        for (Entry<String, String[]> entry : parameterMap.entrySet()) {
                            String parameterName = entry.getKey();
                            if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
                                String[] parameterValues = entry.getValue();
                                if (parameterValues != null) {
                                    for (String parameterValue : parameterValues) {
                                        parameter.append(parameterName).append(" = ").append(parameterValue).append("\n");
                                    }
                                }
                            }
                        }
                    }
                    Log log = new Log();
                    log.setOperation(operation);
                    log.setOperator(operator);
                    log.setContent(content);
                    log.setParameter(parameter.toString());
                    log.setIp(ip);
                    logService.save(log);
                    break;
                }
            }
        }
    }

    /**
     * 设置忽略参数
     *
     * @return 忽略参数
     */
    public String[] getIgnoreParameters() {
        return ignoreParameters;
    }

    /**
     * 设置忽略参数
     *
     * @param ignoreParameters 忽略参数
     */
    public void setIgnoreParameters(String[] ignoreParameters) {
        this.ignoreParameters = ignoreParameters;
    }

}