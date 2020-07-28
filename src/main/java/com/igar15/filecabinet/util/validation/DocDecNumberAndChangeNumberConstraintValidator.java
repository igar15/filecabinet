package com.igar15.filecabinet.util.validation;

import com.igar15.filecabinet.dto.ChangeNoticeTo;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class DocDecNumberAndChangeNumberConstraintValidator implements ConstraintValidator<DocDecNumberAndChangeNumberValid, ChangeNoticeTo> {

   @Autowired
   private DocumentRepository documentRepository;

   public void initialize(DocDecNumberAndChangeNumberValid constraint) {

   }

   public boolean isValid(ChangeNoticeTo userInput, ConstraintValidatorContext context) {

      if (userInput.getTempDocumentDecimalNumber() == null) return true;
      if (userInput.getTempDocumentChangeNumber() == null) return true;

      if (userInput.getTempDocumentDecimalNumber().isEmpty()) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempDocumentDecimalNumber").addConstraintViolation();

         if (userInput.getTempDocumentChangeNumber().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempDocumentChangeNumber").addConstraintViolation();
         }

         return false;
      }

      Document document = documentRepository.findByDecimalNumber(userInput.getTempDocumentDecimalNumber()).orElse(null);

      if (document == null) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("document must exist").addPropertyNode("tempDocumentDecimalNumber").addConstraintViolation();

         if (userInput.getTempDocumentChangeNumber().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempDocumentChangeNumber").addConstraintViolation();
         }

         return false;
      }


      boolean match = userInput.getDocuments().stream()
              .anyMatch(doc -> doc.startsWith(userInput.getTempDocumentDecimalNumber()));

      if (match) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("document already added").addPropertyNode("tempDocumentDecimalNumber").addConstraintViolation();

         if (userInput.getTempDocumentChangeNumber().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must not be empty").addPropertyNode("tempDocumentChangeNumber").addConstraintViolation();
         }

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
