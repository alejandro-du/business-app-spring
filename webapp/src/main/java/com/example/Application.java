package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:/i18n/**-messages**.properties");

            String[] baseNames = Arrays.stream(resources)
                    .map(Resource::getFilename)
                    .map(fileName -> fileName.substring(0, fileName.indexOf("-messages")))
                    .map(baseName -> "classpath:/i18n/" + baseName + "-messages")
                    .collect(Collectors.toSet())
                    .toArray(new String[0]);

            messageSource.setBasenames(baseNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactory(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
