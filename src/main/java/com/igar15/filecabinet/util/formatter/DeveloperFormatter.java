package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DeveloperFormatter implements Formatter<Developer> {

    @Autowired
    private DeveloperService developerService;

    @Override
    public Developer parse(String s, Locale locale) throws ParseException {
        return developerService.findByName(s);
    }

    @Override
    public String print(Developer developer, Locale locale) {
        return developer.getName();
    }
}
