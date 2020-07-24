package com.igar15.filecabinet.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ChangeNoticeNameValidConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeNoticeNameValid {

    public String value() default "";

    public String message() default "change notice must exist";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
