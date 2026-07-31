package org.example.authservice.security;

import org.example.authservice.dto.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityContextUtil {

    public UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new RuntimeException("User is not authenticated");
        }

        return (UserPrincipal) authentication.getPrincipal();
    }


    public UUID getCurrentUserId() {
        return getCurrentUser().getUserId();
    }


    public String getCurrentRole() {
        return getCurrentUser().getRole();
    }
}