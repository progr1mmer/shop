//package com.x.shop.template.directive;
//
//import com.x.shop.entity.Member;
//import com.x.shop.service.MemberService;
//import freemarker.core.Environment;
//import freemarker.template.TemplateDirectiveBody;
//import freemarker.template.TemplateException;
//import freemarker.template.TemplateModel;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.io.Writer;
//import java.util.Map;
//
///**
// * 模板指令 - 当前会员
// *
// * @author progr1mmer
// * @date Created on 2020/2/13
// */
//@Component("currentMemberDirective")
//public class CurrentMemberDirective extends BaseDirective {
//
//    /**
//     * 变量名称
//     */
//    private static final String VARIABLE_NAME = "currentMember";
//
//    @Resource(name = "memberServiceImpl")
//    private MemberService memberService;
//
//    @Override
//    @SuppressWarnings("rawtypes")
//    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
//        Member currentMember = memberService.getCurrent();
//        if (body != null) {
//            setLocalVariable(VARIABLE_NAME, currentMember, env, body);
//        } else {
//            if (currentMember != null) {
//                Writer out = env.getOut();
//                out.write(currentMember.getUsername());
//            }
//        }
//    }
//
//}