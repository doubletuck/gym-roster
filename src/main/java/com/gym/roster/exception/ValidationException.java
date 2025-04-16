package com.gym.roster.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;

public class ValidationException extends RuntimeException {

    @Getter
    private final String errorListText;

    public <T> ValidationException(Set<ConstraintViolation<T>> errors) {
        super("Constraint violations were encountered: " + buildErrorListText(errors));
        this.errorListText = buildErrorListText(errors);
    }

    private static <T> String buildErrorListText(Set<ConstraintViolation<T>> errors) {
        StringBuilder errorsText = new StringBuilder();
        boolean first = true;
        for (ConstraintViolation<T> error : errors) {
            if (first) {
                first = false;
            } else {
                errorsText.append("; ");
            }
            errorsText.append(error.getMessage());
        }
        return errorsText.toString();
    }
}
