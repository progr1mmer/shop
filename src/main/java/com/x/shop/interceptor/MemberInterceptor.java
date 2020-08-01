//package com.x.shop.interceptor;
//
//import com.x.shop.common.Principal;
//import com.x.shop.entity.Member;
//import com.x.shop.service.MemberService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.net.URLEncoder;
//
///**
// * Interceptor - 会员权限
// *
// * @author progr1mmer
// * @date Created on 2020/2/13
// */
//@Component
//public class MemberInterceptor extends HandlerInterceptorAdapter {
//
//    /**
//     * 重定向视图名称前缀
//     */
//    private static final String REDIRECT_VIEW_NAME_PREFIX = "redirect:";
//
//    /**
//     * "重定向URL"参数名称
//     */
//    private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";
//
//    /**
//     * "会员"属性名称
//     */
//    private static final String MEMBER_ATTRIBUTE_NAME = "member";
//
//    /**
//     * 默认登录URL
//     */
//    private static final String DEFAULT_LOGIN_URL = "/login.html";
//
//    /**
//     * 登录URL
//     */
//    private String loginUrl = DEFAULT_LOGIN_URL;
//
//    @Resource(name = "memberServiceImpl")
//    private MemberService memberService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        Principal principal = (Principal) session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
//        if (principal != null) {
//            return true;
//        } else {
//            String requestType = request.getHeader("X-Requested-With");
//            if (requestType != null && "XMLHttpRequest".equalsIgnoreCase(requestType)) {
//                response.addHeader("loginStatus", "accessDenied");
//                response.sendError(HttpServletResponse.SC_FORBIDDEN);
//                return false;
//            } else {
//                if ("GET".equalsIgnoreCase(request.getMethod())) {
//                    String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
//                    response.sendRedirect(request.getContextPath() + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, "UTF-8"));
//                } else {
//                    response.sendRedirect(request.getContextPath() + loginUrl);
//                }
//                return false;
//            }
//        }
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        if (modelAndView != null) {
//            String viewName = modelAndView.getViewName();
//            if (!StringUtils.startsWith(viewName, REDIRECT_VIEW_NAME_PREFIX)) {
//                modelAndView.addObject(MEMBER_ATTRIBUTE_NAME, memberService.getCurrent());
//            }
//        }
//    }
//
//    /**
//     * 获取登录URL
//     *
//     * @return 登录URL
//     */
//    public String getLoginUrl() {
//        return loginUrl;
//    }
//
//    /**
//     * 设置登录URL
//     *
//     * @param loginUrl 登录URL
//     */
//    public void setLoginUrl(String loginUrl) {
//        this.loginUrl = loginUrl;
//    }
//
//}