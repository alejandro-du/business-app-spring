package com.example.api.service;

import com.example.api.BusinessAppModule;
import com.example.api.domain.User;
import com.example.api.domain.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Arrays;
import java.util.Optional;

@Component
@SessionScope
public class AuthenticationService {

    public static final String USER_ID_SESSION_KEY = "user_id";

    private final UserRepository userRepository;
    private final Optional<BusinessAppModule[]> modules;
    private final PasswordEncoder passwordEncoder;

    private boolean modulesInitialized;

    public AuthenticationService(UserRepository userRepository,
                                 Optional<BusinessAppModule[]> modules,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modules = modules;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByEmailIgnoreCase(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getId(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().toString()));
            SecurityContextHolder.getContext().setAuthentication(token);
            VaadinSession.getCurrent().setAttribute(USER_ID_SESSION_KEY, user.getId());

            if (!modulesInitialized && modules.isPresent()) {
                Arrays.stream(modules.get()).forEach(BusinessAppModule::initialize);
                modulesInitialized = true;
            }
            return true;
        }

        return false;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated() &&
                !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

}
