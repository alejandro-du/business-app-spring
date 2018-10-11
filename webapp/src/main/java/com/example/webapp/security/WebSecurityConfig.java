package com.example.webapp.security;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.shared.JsonConstants;
import elemental.json.JsonArray;
import elemental.json.impl.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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
@ConfigurationProperties(prefix = "webapp")
@PropertySource("application.yml")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Map<String, String> access = new HashMap<>();

    private final ApplicationContext applicationContext;

    public WebSecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Map<String, String> getAccess() {
        return access;
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                    "/frontend/**",
                    "/favicon.ico"
                ).permitAll();

                addMatchersFromProperties(registry)
                .requestMatchers(this::isVaadinFlowRequest).permitAll()
                .anyRequest().denyAll()
            .and()
            .addFilterAfter(getAuthFilter(), BasicAuthenticationFilter.class)
            .formLogin()
            .loginPage("/login").permitAll()
            .and()
            .logout().permitAll();
        // @formatter:on
    }

    private Filter getAuthFilter() {
        return new GenericFilterBean() {

            @Override
            public void doFilter(ServletRequest servletRequest,
                                 ServletResponse servletResponse,
                                 FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

                boolean isRequestToRoot = httpServletRequest.getRequestURI().equals("/");
                if (!isVaadinFlowRequest(httpServletRequest) && !isRequestToRoot) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                    CustomServletResponseWrapper wrappedResponse =
                            new CustomServletResponseWrapper(httpServletResponse);
                    filterChain.doFilter(servletRequest, wrappedResponse);
                    String output = wrappedResponse.getBranchOutput();

                    if (wrappedResponse.getContentType() != null &&
                            wrappedResponse.getContentType().startsWith("application/json")) {
                        if (checkFromJsonResponse(servletResponse, output)) {
                            return;
                        }
                    } else if (StringUtils.containsAny(output, "history.pushState", "execute")) {
                        int pushStatePosition = output.indexOf("history.pushState");
                        String firstPart = output.substring(0, pushStatePosition);
                        String lastPart = output.substring(pushStatePosition);

                        int start = firstPart.lastIndexOf("[");
                        int end = lastPart.indexOf("]") + pushStatePosition + 1;

                        String historyJson = output.substring(start, end);
                        JsonArray array = JsonUtil.parse(historyJson);
                        if (checkExecuteElement(array)) {
                            httpServletResponse.sendRedirect("/login");
                            return;
                        }
                    }

                    String originalContent = wrappedResponse.getMasterOutput();
                    servletResponse.getWriter().write(originalContent);
                    servletResponse.setContentLength(originalContent.length() + 1);
                }
            }

        };
    }

    private boolean checkFromJsonResponse(ServletResponse servletResponse, String output) throws IOException {
        if (output.startsWith("for(;;);")) {
            output = output.substring("for(;;);".length());
            JsonArray json = JsonUtil.parse(output);
            JsonArray execute = json.getObject(0).getArray(JsonConstants.UIDL_KEY_EXECUTE);

            if (execute != null) {
                for (int i = 0; i < execute.length(); i++) {
                    JsonArray array = execute.getArray(i);
                    if (checkExecuteElement(array)) {
                        String response = getRedirectToLoginResponse();
                        servletResponse.getWriter().write(response);
                        servletResponse.setContentLength(response.length());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkExecuteElement(JsonArray array) {
        if (array.length() == 3 && array.getString(2).startsWith("history.pushState") && array.getString(1) != null &&
                !array.getString(1).isEmpty()) {
            String location = "/" + array.getString(1);
            WebInvocationPrivilegeEvaluator privilegeEvaluator =
                    applicationContext.getBean(WebInvocationPrivilegeEvaluator.class);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return !privilegeEvaluator.isAllowed(location, authentication);
        }
        return false;
    }

    private String getRedirectToLoginResponse() {
        String script = "location='/login'";
        return "for(;;);[{\"execute\":[[\"" + script + "\"]]}]";
    }

    private boolean isVaadinFlowRequest(HttpServletRequest request) {
        try {
            String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
            return parameterValue != null && Stream.of(ServletHelper.RequestType.values())
                    .map(ServletHelper.RequestType::getIdentifier)
                    .anyMatch(parameterValue::equals);
        } catch (Exception ignored) {
        }

        return false;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry addMatchersFromProperties(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
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
        }

        return registry;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry addAntMatcher(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry,
            String routeValue,
            String accessExpression) {
        String antPattern;
        if (routeValue.isEmpty()) {
            antPattern = "/";
        } else {
            antPattern = "/" + routeValue + "/**";
        }

        return registry.antMatchers(antPattern).access(accessExpression);
    }

}
