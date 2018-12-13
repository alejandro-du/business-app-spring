package com.example.common.service;

import com.example.common.BusinessAppModule;
import com.example.common.domain.User;
import com.example.common.domain.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthenticationService {

    public static final String USER_ID_SESSION_KEY = "user_id";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationContext applicationContext;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 ApplicationContext applicationContext) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationContext = applicationContext;
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByEmailIgnoreCase(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getId(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().toString())
            );
            SecurityContextHolder.getContext().setAuthentication(token);
            VaadinSession.getCurrent().setAttribute(USER_ID_SESSION_KEY, user.getId());

            initModules();
            return true;
        }

        return false;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated() &&
                !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

    private void initModules() {
        Map<String, BusinessAppModule> modules = applicationContext.getBeansOfType(BusinessAppModule.class);
        modules.entrySet().stream().map(entry -> entry.getValue()).forEach(BusinessAppModule::initialize);
    }

}
