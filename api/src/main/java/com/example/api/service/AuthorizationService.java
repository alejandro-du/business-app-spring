package com.example.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationService {

    private final WebInvocationPrivilegeEvaluator privilegeEvaluator;

    public AuthorizationService(WebInvocationPrivilegeEvaluator privilegeEvaluator) {
        this.privilegeEvaluator = privilegeEvaluator;
    }

    public boolean userCanAccess(String url) {
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return privilegeEvaluator.isAllowed(url, authentication);
    }

}
