package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MapDocumentIntegerFormatter implements Formatter<TreeMap<Document, Integer>> {

    @Autowired
    private DocumentService documentService;

    @Override
    public TreeMap<Document, Integer> parse(String s, Locale locale) throws ParseException {
        TreeMap<Document, Integer> resultMap = new TreeMap<>(Comparator.comparing(Document::getDecimalNumber));

        if (s.equals("{}")) {
            return resultMap;
        }

        String s1 = s.replaceAll("\\{", "").replaceAll("}", "");
        String[] split = s1.split(", ");
        for (String tempSplit : split) {
            resultMap.put(documentService.findByDecimalNumber(tempSplit.split("=")[0]), Integer.parseInt(tempSplit.split("=")[1]));
        }
        return resultMap;
    }

    @Override
    public String print(TreeMap<Document, Integer> documentIntegerMap, Locale locale) {
        String mapAsString = documentIntegerMap.keySet().stream()
                .map(key -> key + "=" + documentIntegerMap.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }
}
