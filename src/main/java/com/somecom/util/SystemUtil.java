package com.somecom.util;

import com.somecom.entity.SysUser;
import com.somecom.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class SystemUtil {
    private static final String CURRENT_USER = "CURRENT_USER";
    public static final String CURRENT_ADMIN = "CURRENT_ADMIN";

    public static Optional<SysUser> currentAdmin() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return Optional.ofNullable((SysUser) (attr.getRequest().getSession(true).getAttribute(CURRENT_ADMIN)));
    }

    public static void adminLogin(SysUser user) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attr.getRequest().getSession(true).setAttribute(CURRENT_ADMIN, user);
    }

    public static Optional<User> currentUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return Optional.ofNullable((User) (attr.getRequest().getSession(true).getAttribute(CURRENT_USER)));
    }

    public static void login(User user) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attr.getRequest().getSession(true).setAttribute(CURRENT_USER, user);
    }

    public static void adminLogout() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attr.getRequest().getSession(true).removeAttribute(CURRENT_ADMIN);
    }
}
