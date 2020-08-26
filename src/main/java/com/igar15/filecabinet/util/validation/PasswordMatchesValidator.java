package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

   @Override
   public void initialize(final PasswordMatches constraintAnnotation) {
      //
   }

   @Override
   public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
      final User user = (User) obj;
      return user.getPassword()
              .equals(user.getPasswordConfirmation());
   }

}