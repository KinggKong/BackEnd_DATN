package com.example.be_datn.utils;


import com.example.be_datn.config.AccountDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentUserId() {
        return getCurrentUser().getAccount().getOwnerID();
    }

    public static String getCurrentUsername() {
        return getCurrentUser().getAccount().getTenDangNhap();
    }

    public static AccountDetailsImpl getCurrentUser() {
        if (getCurrentAuthentication() != null)
            return (AccountDetailsImpl) getCurrentAuthentication().getPrincipal();
        return null;
    }

    private static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        return getCurrentAuthentication().isAuthenticated();
    }

    public static boolean hasRole(String role) {
        return getCurrentAuthentication().getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    }
}
