package com.haubir.nativeimage.cli.model.hello;

import io.vavr.control.Validation;

import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.apache.commons.lang3.Validate.notBlank;

public record FirstName(String value) {
    public FirstName {
        if (!isAlpha(value)) throw new IllegalArgumentException("First name can only contain letters.");
        notBlank(value);
    }

    public static Validation<String, FirstName> validate(String value) {
        try {
            return Validation.valid(new FirstName(value));
        } catch (final Exception e) {
            return Validation.invalid("Invalid first name");
        }
    }
}
