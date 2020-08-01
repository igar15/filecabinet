package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlbumEntryValidConstraintValidator implements ConstraintValidator<AlbumEntryValid, String> {

   @Autowired
   private DocumentRepository documentRepository;

   private String decimalNumber;

   @Override
   public void initialize(AlbumEntryValid constraintAnnotation) {
      decimalNumber = constraintAnnotation.value();
   }

   @Override
   public boolean isValid(String theUserDecNumber, ConstraintValidatorContext constraintValidatorContext) {
      if(theUserDecNumber == null) return true;
      if(theUserDecNumber.equals("Singly")) return true;
      Document document = documentRepository.findByDecimalNumber(theUserDecNumber).orElse(null);
      return document != null;
   }
}
