package com.igar15.filecabinet.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChangeNoticeNameAndDocChangeNumberConstraintValidator.class)
public @interface ChangeNoticeNameAndDocChangeNumberValid {

    public String tempChangeNoticeName();

    public String tempChangeNoticeNumber();

    public String message() default "the document already has such change number!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
