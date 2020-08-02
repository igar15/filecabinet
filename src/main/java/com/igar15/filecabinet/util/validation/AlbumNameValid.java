package com.igar15.filecabinet.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AlbumNameConstraintValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AlbumNameValid {

//    public String decNumber();
//
//    public String changeNumber();

    public String message() default "the document (album name) must exist!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
