package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class AlbumNameValidConstraintValidator implements ConstraintValidator<AlbumNameValid, InternalDispatch> {

   @Autowired
   private DocumentService documentService;


   public void initialize(AlbumNameValid constraint) {
   }

   public boolean isValid(InternalDispatch obj, ConstraintValidatorContext context) {

      if (!obj.getIsAlbum()) {
         return true;
      }

      Document document = null;
      try {
         document = documentService.findByDecimalNumber(obj.getAlbumName());
      } catch (NotFoundException e) {
      }

      if (document == null) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Document does not exist").addPropertyNode("albumName").addConstraintViolation();
         return false;
      }
      return true;
   }
}
