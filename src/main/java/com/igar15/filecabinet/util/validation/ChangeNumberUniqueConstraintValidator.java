package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.dto.ChangeNoticeTo;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ChangeNumberUniqueConstraintValidator implements ConstraintValidator<ChangeNumberUniqueValid, ChangeNoticeTo> {

   @Autowired
   private DocumentRepository documentRepository;

   private String decNumber;

   private String changeNumber;




   public void initialize(ChangeNumberUniqueValid constraint) {
      decNumber = constraint.decNumber();
      changeNumber = constraint.changeNumber();

   }

   public boolean isValid(ChangeNoticeTo userInput, ConstraintValidatorContext context) {

      if (userInput.getTempDocumentDecimalNumber() == null) return true;
      //if (userInput.getTempDocumentDecimalNumber().isEmpty()) return true;
      if (userInput.getTempDocumentChangeNumber() == null) return true;

      if (userInput.getTempDocumentDecimalNumber().isEmpty()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempDocumentDecimalNumber").addConstraintViolation();
         return false;
      }

      Document document = documentRepository.findByDecimalNumber(userInput.getTempDocumentDecimalNumber()).orElse(null);

      if (document == null) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("document must exist").addPropertyNode("tempDocumentDecimalNumber").addConstraintViolation();
         return false;
      }

//      final Boolean[] isAlreadyContains = {false};

      boolean match = userInput.getDocuments().stream()
              .anyMatch(doc -> doc.startsWith(userInput.getTempDocumentDecimalNumber()));

//      userInput.getDocuments().forEach(doc -> {
//         if (doc.startsWith(userInput.getTempDocumentDecimalNumber())) {
//            isAlreadyContains[0] = true;
//         }
//      });
      if (match) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("document already added").addPropertyNode("tempDocumentDecimalNumber").addConstraintViolation();
         return false;
      }


      if (userInput.getTempDocumentChangeNumber().isEmpty()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempDocumentChangeNumber").addConstraintViolation();
         return false;
      }

      boolean notContainsNumber = !document.getChangeNotices().containsKey(Integer.parseInt(userInput.getTempDocumentChangeNumber()));
      if (!notContainsNumber) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode("tempDocumentChangeNumber").addConstraintViolation();
      }
      return notContainsNumber;
   }

}
