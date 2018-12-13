package com.example.common.service;

import com.example.common.domain.User;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.spring.annotation.SpringComponent;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@SpringComponent
public class ValidationService {

    private final ValidatorFactory validatorFactory;

    public ValidationService(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    public void validateProperty(User user, String propertyName, HasValidation field) throws
            javax.validation.ValidationException {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validateProperty(user, propertyName);

        if (!violations.isEmpty()) {
            ConstraintViolation<User> violation = violations.iterator().next();
            field.setErrorMessage(violation.getMessage());
            field.setInvalid(true);
            throw new javax.validation.ValidationException();
        }
    }

}
