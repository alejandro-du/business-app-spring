package com.example.common.service;

import com.example.common.BusinessAppModule;
import com.example.common.domain.Role;
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
import java.util.Optional;

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
                    AuthorityUtils.createAuthorityList(getAuthority(user.getRole()))
            );
            SecurityContextHolder.getContext().setAuthentication(token);
            VaadinSession.getCurrent().setAttribute(USER_ID_SESSION_KEY, user.getId());

            initModules();
            return true;
        }

        return false;
    }

    private String getAuthority(Role role) {
        return "ROLE_" + role.toString();
    }

    public void reAuthenticateCurrentUser() {
        if (isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = (Long) authentication.getPrincipal();
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                Optional<String> authority =
                        authentication.getAuthorities().stream().map(a -> a.getAuthority()).findFirst();
                if (authority.isPresent()) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userId,
                            authentication.getCredentials(),
                            AuthorityUtils.createAuthorityList(getAuthority(user.get().getRole()))
                    );
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        }
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }

    private void initModules() {
        Map<String, BusinessAppModule> modules = applicationContext.getBeansOfType(BusinessAppModule.class);
        modules.entrySet().stream().map(entry -> entry.getValue()).forEach(BusinessAppModule::initialize);
    }

}
