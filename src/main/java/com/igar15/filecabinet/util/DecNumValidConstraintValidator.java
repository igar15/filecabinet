package com.igar15.filecabinet.util;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class DecNumValidConstraintValidator implements ConstraintValidator<DecNumValid, String> {

    @Autowired
    private DocumentRepository documentRepository;

    private String decimalNumber;

    @Override
    public void initialize(DecNumValid constraintAnnotation) {
        decimalNumber = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String theUserDecNumber, ConstraintValidatorContext constraintValidatorContext) {
        if(theUserDecNumber == null) return true;
        Document document = documentRepository.findByDecimalNumber(theUserDecNumber).orElse(null);
        return document != null;
    }
}
