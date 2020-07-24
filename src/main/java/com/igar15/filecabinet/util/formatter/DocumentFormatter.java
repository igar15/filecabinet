package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DocumentFormatter implements Formatter<Document> {

    @Autowired
    private DocumentService documentService;

    @Override
    public Document parse(String s, Locale locale) throws ParseException {
        return documentService.findByDecimalNumber(s);
    }

    @Override
    public String print(Document document, Locale locale) {
        return document.getDecimalNumber();
    }
}
