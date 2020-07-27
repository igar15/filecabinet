package com.igar15.filecabinet.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ChangeNumberUniqueConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeNumberUniqueValid {

    public String decNumber();

    public String changeNumber();

    public String message() default "the document already has such change number!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
