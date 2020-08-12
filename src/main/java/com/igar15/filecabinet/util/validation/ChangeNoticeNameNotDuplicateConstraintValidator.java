package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ChangeNoticeNameNotDuplicateConstraintValidator implements ConstraintValidator<ChangeNoticeNameNotDuplicate, ChangeNotice> {

   @Autowired
   private ChangeNoticeService changeNoticeService;


   public void initialize(ChangeNoticeNameNotDuplicate constraint) {
   }

   public boolean isValid(ChangeNotice obj, ConstraintValidatorContext context) {

      ChangeNotice changeNotice = null;
      try {
         changeNotice = changeNoticeService.findByName(obj.getName());
      } catch (NotFoundException e) {
         return true;
      }
      if (obj.isNew()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Name already exists").addPropertyNode("name").addConstraintViolation();
         return false;
      }
      else if (!obj.getId().equals(changeNotice.getId())) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Name already exists").addPropertyNode("name").addConstraintViolation();
         return false;
      }
      return true;
   }
}
