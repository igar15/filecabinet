package com.igar15.filecabinet.util;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.service.ChangeNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ChangeNoticeNameValidConstraintValidator implements ConstraintValidator<ChangeNoticeNameValid, String> {

    @Autowired
    private ChangeNoticeRepository changeNoticeRepository;

    private String changeNoticeName;

    @Override
    public void initialize(ChangeNoticeNameValid constraintAnnotation) {
        changeNoticeName = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String theUserChangeNotName, ConstraintValidatorContext constraintValidatorContext) {
        if (theUserChangeNotName == null) return false;
        ChangeNotice changeNotice = changeNoticeRepository.findByName(theUserChangeNotName).orElse(null);
        return changeNotice != null;
    }
}
