package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class DecimalNumberNotDuplicateConstraintValidator implements ConstraintValidator<DecimalNumberNotDuplicate, Document> {

   @Autowired
   private DocumentService documentService;

   public void initialize(DecimalNumberNotDuplicate constraint) {
   }

   public boolean isValid(Document obj, ConstraintValidatorContext context) {
      if (documentService == null) {
         return true;
      }
      Document document = null;
      try {
         document = documentService.findByDecimalNumber(obj.getDecimalNumber());
      } catch (NotFoundException e) {
         return true;
      }
      if (obj.isNew()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Decimal number already exists").addPropertyNode("decimalNumber").addConstraintViolation();
         return false;
      }
      else if (!obj.getId().equals(document.getId())) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Decimal number already exists").addPropertyNode("decimalNumber").addConstraintViolation();
         return false;
      }
      return true;
   }
}
