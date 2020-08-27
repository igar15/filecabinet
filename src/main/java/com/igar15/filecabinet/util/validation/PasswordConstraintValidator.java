package com.igar15.filecabinet.util.validation;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

   @Override
   public void initialize(final ValidPassword arg0) {
   }

   @Override
   public boolean isValid(final String password, final ConstraintValidatorContext context) {
      final PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 30), new UppercaseCharacterRule(1), new DigitCharacterRule(1), new SpecialCharacterRule(1), new WhitespaceRule()));
      final RuleResult result = validator.validate(new PasswordData(password));
      if (result.isValid()) {
         return true;
      }
      context.disableDefaultConstraintViolation();
      StringBuilder stringBuilder = new StringBuilder();
      validator.getMessages(result).forEach(message -> stringBuilder.append(message).append("\n"));
      context.buildConstraintViolationWithTemplate(stringBuilder.toString())
              .addConstraintViolation();
      return false;
   }

}
