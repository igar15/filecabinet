package com.igar15.filecabinet.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DecNumUniqueConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecNumUnique {

    public String value() default "";

    public String message() default "decimal number must be unique";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
