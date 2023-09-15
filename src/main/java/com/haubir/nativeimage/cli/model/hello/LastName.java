package com.haubir.nativeimage.cli.model.hello;

import io.vavr.control.Validation;

import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.apache.commons.lang3.Validate.notBlank;

public record LastName(String value) {
    public LastName {
        if (!isAlpha(value)) throw new IllegalArgumentException("Last name can only contain letters.");
        notBlank(value);
    }

    public static Validation<String, LastName> validate(String value) {
        try {
            return Validation.valid(new LastName(value));
        } catch (final Exception e) {
            return Validation.invalid("Invalid last name");
        }
    }
}
