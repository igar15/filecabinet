package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CompanyFormatter implements Formatter<Company> {

    @Autowired
    private CompanyService companyService;

    @Override
    public Company parse(String s, Locale locale) throws ParseException {
        return companyService.findByName(s);
    }

    @Override
    public String print(Company company, Locale locale) {
        return company.getName();
    }
}
