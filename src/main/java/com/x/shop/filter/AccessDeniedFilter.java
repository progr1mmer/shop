package com.x.shop.filter;

import sun.misc.BASE64Decoder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter - 限制访问
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@WebFilter(filterName = "accessDeniedFilter", urlPatterns = {"/install/data/**", "/install/sample/**"})
public class AccessDeniedFilter implements Filter {

    /**
     * 错误消息
     */
    private static final String ERROR_MESSAGE = "Access denied!";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader(new String(new BASE64Decoder().decodeBuffer("UG93ZXJlZEJ5"), "utf-8"), new String(new BASE64Decoder().decodeBuffer("U2hvcHh4Lm5ldA=="), "utf-8"));
        response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MESSAGE);
    }

    @Override
    public void destroy() {
    }
}