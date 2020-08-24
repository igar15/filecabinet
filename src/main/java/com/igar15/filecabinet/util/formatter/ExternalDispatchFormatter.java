package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.repository.ExternalDispatchRepository;
import com.igar15.filecabinet.service.ExternalDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class ExternalDispatchFormatter implements Formatter<ExternalDispatch> {

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @Override
    public ExternalDispatch parse(String s, Locale locale) throws ParseException {
        return externalDispatchService.findById(Integer.parseInt(s));
    }

    @Override
    public String print(ExternalDispatch externalDispatch, Locale locale) {
        return String.valueOf(externalDispatch.getId());
    }
}
