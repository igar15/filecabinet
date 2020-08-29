package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.service.UserService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotDuplicateConstraintValidator implements ConstraintValidator<EmailNotDuplicate, User> {

   @Autowired
   private UserService userService;


   public void initialize(EmailNotDuplicate constraint) {
      //
   }

   public boolean isValid(User obj, ConstraintValidatorContext context) {
     User user = null;
      try {
         user = userService.findByEmail(obj.getEmail());
      } catch (NotFoundException e) {
         return true;
      }
      if (obj.isNew()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Email already registered").addPropertyNode("email").addConstraintViolation();
         return false;
      }
      else if (!obj.getId().equals(user.getId())) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Email already registered").addPropertyNode("email").addConstraintViolation();
         return false;
      }
      return true;
   }
}
