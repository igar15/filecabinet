package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.dto.DocumentTo;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ChangeNoticeNameAndDocChangeNumberConstraintValidator implements ConstraintValidator<ChangeNoticeNameAndDocChangeNumberValid, DocumentTo> {

   @Autowired
   private ChangeNoticeRepository changeNoticeRepository;

   public void initialize(ChangeNoticeNameAndDocChangeNumberValid constraint) {
   }

   public boolean isValid(DocumentTo obj, ConstraintValidatorContext context) {
      if (obj.getTempChangeNoticeName() == null || obj.getTempChangeNoticeNumber() == null) return true;

      if (obj.getTempChangeNoticeName().isEmpty()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempChangeNoticeName").addConstraintViolation();

         if (obj.getTempChangeNoticeNumber().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempChangeNoticeNumber").addConstraintViolation();
         }
         return false;
      }

      ChangeNotice changeNotice = changeNoticeRepository.findByName(obj.getTempChangeNoticeName()).orElse(null);

      if (changeNotice == null) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("change notice must exist").addPropertyNode("tempChangeNoticeName").addConstraintViolation();

         if (obj.getTempChangeNoticeNumber().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempChangeNoticeNumber").addConstraintViolation();
         }
         return false;
      }

//      boolean match = obj.getChangeNotices().stream()
//              .anyMatch(change -> change.startsWith(obj.getTempChangeNoticeName()));
      boolean match = obj.getChangeNotices().values().stream()
              .anyMatch(change -> change.getName().startsWith(obj.getTempChangeNoticeName()));

      if (match) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("change notice already added").addPropertyNode("tempChangeNoticeName").addConstraintViolation();

         if (obj.getTempChangeNoticeNumber().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempChangeNoticeNumber").addConstraintViolation();
         }
         return false;
      }

      if (obj.getTempChangeNoticeNumber().isEmpty()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempChangeNoticeNumber").addConstraintViolation();
         return false;
      }

//      boolean changeNumberExist = obj.getChangeNotices().stream()
//              .anyMatch(change -> change.endsWith("ch. " + obj.getTempChangeNoticeNumber()));
      boolean changeNumberExist = obj.getChangeNotices().containsKey(Integer.parseInt(obj.getTempChangeNoticeNumber()));

      if (changeNumberExist) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("this change number already exist!").addPropertyNode("tempChangeNoticeNumber").addConstraintViolation();
         return false;
      }

      return true;

   }
}
