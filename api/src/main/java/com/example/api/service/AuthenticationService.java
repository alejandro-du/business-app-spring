package com.example.api.service;

import com.example.api.BusinessAppModule;
import com.example.api.domain.User;
import com.example.api.domain.UserRepository;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@Component
@SessionScope
public class AuthenticationService {

    public static final String USER_ID_SESSION_KEY = "user_id";

    private final UserRepository userRepository;

    private final Optional<BusinessAppModule[]> modules;

    private boolean modulesInitialized;

    public AuthenticationService(UserRepository userRepository, Optional<BusinessAppModule[]> modules) {
        this.userRepository = userRepository;
        this.modules = modules;
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByEmailIgnoreCaseAndPassword(username, password);

        if (user == null) {
            return false;
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getId(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().toString())
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        VaadinSession.getCurrent().setAttribute(USER_ID_SESSION_KEY, user.getId());

        if (!modulesInitialized && modules.isPresent()) {
            Arrays.stream(modules.get()).forEach(BusinessAppModule::initialize);
            modulesInitialized = true;
        }

        return true;
    }

    public void logout() {
        HttpServletRequest request = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest();
        new SecurityContextLogoutHandler().logout(request, null, null);
        VaadinService.getCurrentRequest().getWrappedSession().invalidate();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated() && !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

}
