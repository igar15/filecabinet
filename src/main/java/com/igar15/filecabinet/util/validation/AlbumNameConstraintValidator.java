package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.dto.InternalDispatchTo;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class AlbumNameConstraintValidator implements ConstraintValidator<AlbumNameValid, InternalDispatchTo> {

   @Autowired
   private DocumentRepository documentRepository;

   public void initialize(AlbumNameValid constraint) {
   }

   public boolean isValid(InternalDispatchTo obj, ConstraintValidatorContext context) {
      if (obj.getIsAlbum() == null) {
         return false;
      }
      if (obj.getIsAlbum()) {
         Document document = documentRepository.findByDecimalNumber(obj.getAlbumName()).orElse(null);
         if (document == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("document must exist (album name)").addPropertyNode("albumName").addConstraintViolation();
            if (obj.getStamp() == null) {
               context.buildConstraintViolationWithTemplate("stamp for album must not be null").addPropertyNode("stamp").addConstraintViolation();
            }
            return false;
         } else {
            if (obj.getStamp() == null) {
               context.disableDefaultConstraintViolation();
               context.buildConstraintViolationWithTemplate("stamp for album must not be null").addPropertyNode("stamp").addConstraintViolation();
               return false;
            }
            return true;
         }
      }
      else {
         if (obj.getAlbumName() == null || obj.getAlbumName().isEmpty()) {
            return true;
         }
         else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("album name must be null cuse its not an album").addPropertyNode("albumName").addConstraintViolation();
            return false;
         }
      }
   }
}
