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
      if (user.getPassword() == null) return true;
      boolean isPasswordsMatches = user.getPassword()
              .equals(user.getPasswordConfirmation());
      if (isPasswordsMatches) {
         return true;
      }
      else {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Passwords do not match").addPropertyNode("passwordConfirmation").addConstraintViolation();
         context.buildConstraintViolationWithTemplate("Passwords do not match").addPropertyNode("password").addConstraintViolation();
         return false;
      }
   }

}