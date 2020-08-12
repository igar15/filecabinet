package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class AlbumNameValidConstraintValidator implements ConstraintValidator<AlbumNameValid, String> {

   @Autowired
   private DocumentService documentService;

   private String albumName;

   public void initialize(AlbumNameValid constraint) {
      albumName = constraint.albumName();
   }

   public boolean isValid(String obj, ConstraintValidatorContext context) {
      if (documentService == null) {
         return true;
      }
      Document document = null;
      try {
         document = documentService.findByDecimalNumber(obj);
      } catch (NotFoundException e) {

      }
      return document != null;
   }
}
