package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class DecNumUniqueConstraintValidator implements ConstraintValidator<DecNumUnique, String> {

   @Autowired
   private DocumentRepository documentRepository;

   private String decimalNumber;

   public void initialize(DecNumUnique constraint) {
      decimalNumber = constraint.value();
   }

   public boolean isValid(String obj, ConstraintValidatorContext context) {
      if (documentRepository.findByDecimalNumber(obj) != null) {
         return false;
      }
      return true;
   }
}
