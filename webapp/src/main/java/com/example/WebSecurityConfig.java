package com.example;

import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(
                    "/VAADIN/**",
                    "/frontend/**"
                ).permitAll()
                .requestMatchers(
                    request -> {
                        String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
                        return parameterValue != null
                                && Stream.of(ServletHelper.RequestType.values())
                                .map(r -> r.getIdentifier())
                                .anyMatch(parameterValue::equals);
                    }
                ).permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/" + LoginView.VIEW_NAME).permitAll()
            .and()
            .logout().permitAll();
        // @formatter:on
    }

}
