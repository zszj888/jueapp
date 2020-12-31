//package com.somecom.shiro.config;
//
//import com.somecom.entity.SysUser;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.UnavailableSecurityManagerException;
//import org.apache.shiro.subject.Subject;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.lang.NonNull;
//
//import java.util.Optional;
//
///**
// * 审核员自动赋值配置
// *
// * @author Sam
// * @date 2018/8/14
// */
//@Configuration
//public class AuditorConfig implements AuditorAware<SysUser> {
//
//    @Override
//    @NonNull
//    public Optional<SysUser> getCurrentAuditor() {
//        try {
//            Subject subject = SecurityUtils.getSubject();
//            SysUser user = (SysUser) subject.getPrincipal();
//            return Optional.ofNullable(user);
//        } catch (UnavailableSecurityManagerException e) {
//            return Optional.empty();
//        }
//    }
//}
