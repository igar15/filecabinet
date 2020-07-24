package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.service.ChangeNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class ChangeNoticeFormatter implements Formatter<ChangeNotice> {

    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Override
    public ChangeNotice parse(String s, Locale locale) throws ParseException {
        return changeNoticeService.findByName(s);
    }

    @Override
    public String print(ChangeNotice changeNotice, Locale locale) {
        return changeNotice.getName();
    }
}
