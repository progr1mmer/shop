package com.x.shop.interceptor;

import com.x.shop.util.WebUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Interceptor - 令牌
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    /**
     * "令牌"属性名称
     */
    private static final String TOKEN_ATTRIBUTE_NAME = "token";

    /**
     * "令牌"Cookie名称
     */
    private static final String TOKEN_COOKIE_NAME = "token";

    /**
     * "令牌"参数名称
     */
    private static final String TOKEN_PARAMETER_NAME = "token";

    /**
     * 错误消息
     */
    private static final String ERROR_MESSAGE = "Bad or missing token!";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = WebUtils.getCookie(request, TOKEN_COOKIE_NAME);
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String requestType = request.getHeader("X-Requested-With");
            if (requestType != null && "XMLHttpRequest".equalsIgnoreCase(requestType)) {
                if (token != null && token.equals(request.getHeader(TOKEN_PARAMETER_NAME))) {
                    return true;
                } else {
                    response.addHeader("tokenStatus", "accessDenied");
                }
            } else {
                if (token != null && token.equals(request.getParameter(TOKEN_PARAMETER_NAME))) {
                    return true;
                }
            }
            if (token == null) {
                token = UUID.randomUUID().toString();
                WebUtils.addCookie(request, response, TOKEN_COOKIE_NAME, token);
            }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MESSAGE);
            return false;
        } else {
            if (token == null) {
                token = UUID.randomUUID().toString();
                WebUtils.addCookie(request, response, TOKEN_COOKIE_NAME, token);
            }
            request.setAttribute(TOKEN_ATTRIBUTE_NAME, token);
            return true;
        }
    }

}