package com.example.webapp.security;

import com.example.webapp.ui.LoginView;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.shared.JsonConstants;
import elemental.json.JsonArray;
import elemental.json.impl.JsonUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@Component
@ConfigurationProperties(prefix = "webapp")
@PropertySource("application.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Map<String, String> access = new HashMap<>();

    private final ApplicationContext applicationContext;

    public WebSecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Map<String, String> getAccess() {
        return access;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
            .csrf().disable()
                .exceptionHandling()
                .accessDeniedPage("/login")
            .and()
            .authorizeRequests()
                .antMatchers(
                    "/VAADIN/**",
                    "/frontend/**"
                ).permitAll();

                addMatchersFromProperties(registry)
                .requestMatchers(this::isVaadinFlowRequest).permitAll()
                .anyRequest().denyAll()
            .and()
            .addFilterAfter(getFilter(), BasicAuthenticationFilter.class)
            .formLogin()
            .loginPage("/" + LoginView.VIEW_NAME).permitAll()
            .and()
            .logout().permitAll();
        // @formatter:on
    }

    private Filter getFilter() {
        return new GenericFilterBean() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                CustomServletResponseWrapper wrappedResponse = new CustomServletResponseWrapper((HttpServletResponse) servletResponse);
                filterChain.doFilter(servletRequest, wrappedResponse);

                if (wrappedResponse.getContentType() != null && wrappedResponse.getContentType().startsWith("application/json")) {
                    String output = wrappedResponse.getBranch().toString();
                    wrappedResponse.getBranch().close();

                    if (output.startsWith("for(;;);")) {
                        output = output.substring("for(;;);".length());
                        JsonArray json = JsonUtil.parse(output);
                        JsonArray execute = json.getObject(0).getArray(JsonConstants.UIDL_KEY_EXECUTE);

                        if (execute != null) {
                            for (int i = 0; i < execute.length(); i++) {
                                JsonArray array = execute.getArray(i);
                                if (array.length() == 3 && array.getString(2).startsWith("history.pushState") && array.getString(1) != null && !array.getString(1).isEmpty()) {
                                    String location = "/" + array.getString(1);
                                    WebInvocationPrivilegeEvaluator privilegeEvaluator = applicationContext.getBean(WebInvocationPrivilegeEvaluator.class);
                                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                                    if (!privilegeEvaluator.isAllowed(location, authentication)) {
                                        String response = getRedirectToLoginResponse();
                                        servletResponse.getWriter().write(response);
                                        servletResponse.setContentLength(response.length());
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }

                String original = wrappedResponse.getMaster().toString();
                servletResponse.getWriter().write(original);
                servletResponse.setContentLength(original.length());
                wrappedResponse.getMaster().close();
            }
        };
    }

    private String getRedirectToLoginResponse() {
        String script = "location='/login'";
        return "for(;;);[{\"execute\":[[\"" + script + "\"]]}]";
    }

    private boolean isVaadinFlowRequest(HttpServletRequest request) {
        try {
            String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
            return parameterValue != null
                    && Stream.of(ServletHelper.RequestType.values())
                    .map(r -> r.getIdentifier())
                    .anyMatch(parameterValue::equals);
        } catch (Exception ignored) {
        }

        return false;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry addMatchersFromProperties(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        try {
            for (Map.Entry<String, String> entry : access.entrySet()) {
                String className = entry.getKey();
                String accessExpression = entry.getValue();

                Class<?> clazz = Class.forName(className);
                String routeValue = Router.resolve(clazz, clazz.getAnnotation(Route.class));

                registry = addAntMatcher(registry, routeValue, accessExpression);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO log e
        }

        return registry;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry addAntMatcher(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry, String routeValue, String accessExpression) throws ClassNotFoundException {
        String antPattern;
        if (routeValue.isEmpty()) {
            antPattern = "/";
        } else {
            antPattern = "/" + routeValue + "/**";
        }

        return registry.antMatchers(antPattern).access(accessExpression);
    }

}
