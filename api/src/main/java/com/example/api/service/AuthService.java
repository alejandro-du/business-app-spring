package com.example.api.service;

import com.example.api.BusinessAppModule;
import com.example.api.domain.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@VaadinSessionScope
public class AuthService {

    private final UserService userService;
    private final BusinessAppModule[] modules;

    public AuthService(UserService userService, BusinessAppModule[] modules) {
        this.userService = userService;
        this.modules = modules;
    }

    public boolean authenticate(String username, String password) {
        User user = userService.findByEmailAndPassword(username, password);

        if (user == null) {
            return false;
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString())
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        VaadinSession.getCurrent().setAttribute("user_id", user.getId());

        Arrays.stream(modules).forEach(BusinessAppModule::initialize);

        return true;
    }

    public void logout() {
        HttpServletRequest request = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest();
        new SecurityContextLogoutHandler().logout(request, null, null);
        UI.getCurrent().getPage().executeJavaScript("location.reload();");
        VaadinService.getCurrentRequest().getWrappedSession().invalidate();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated() && !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass()) ? null : (String) authentication.getPrincipal();
    }

}
