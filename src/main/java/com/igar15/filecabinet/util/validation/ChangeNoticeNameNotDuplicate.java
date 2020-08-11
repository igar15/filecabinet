package com.igar15.filecabinet.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ChangeNoticeNameNotDuplicateConstraintValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeNoticeNameNotDuplicate {

    String message() default "Name already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
