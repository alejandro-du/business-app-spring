package com.example.api.service;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.api.domain.UserRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;

import java.util.Optional;

@SpringComponent
public class AuthorizationService {

    private final WebInvocationPrivilegeEvaluator privilegeEvaluator;
    private final UserRepository userRepository;

    public AuthorizationService(WebInvocationPrivilegeEvaluator privilegeEvaluator, UserRepository userRepository) {
        this.privilegeEvaluator = privilegeEvaluator;
        this.userRepository = userRepository;
    }

    public boolean userCanAccess(Class<? extends Component> viewClass) {
        Route routeAnnotation = viewClass.getAnnotation(Route.class);
        String url = "/" + routeAnnotation.value();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return privilegeEvaluator.isAllowed(url, authentication);
    }

    public Optional<User> getAuthenticatedUser() {
        Long userId = (Long) VaadinSession.getCurrent().getAttribute(AuthenticationService.USER_ID_SESSION_KEY);
        if (userId == null) {
            return Optional.empty();
        }

        return userRepository.findById(userId);
    }

    public Component secureComponent(Component component, Role... allowedRoles) {
        Optional<User> user = getAuthenticatedUser();

        if (user.isPresent()) {
            for (Role role : allowedRoles) {
                if (role.equals(user.get().getRole())) {
                    return component;
                }
            }
        }

        component.setVisible(false);
        return component;
    }

}
