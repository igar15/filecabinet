package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class InternalDispatchFormatter implements Formatter<InternalDispatch> {

    @Autowired
    private InternalDispatchRepository internalDispatchRepository;

    @Override
    public InternalDispatch parse(String s, Locale locale) throws ParseException {
        return internalDispatchRepository.findById(Integer.valueOf(s)).orElse(null);
    }

    @Override
    public String print(InternalDispatch internalDispatch, Locale locale) {
        return String.valueOf(internalDispatch.getId());
    }
}
