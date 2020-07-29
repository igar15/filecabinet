package com.igar15.filecabinet.util.formatter;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.service.ChangeNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapChangeFormatter implements Formatter<Map<Integer, ChangeNotice>> {

    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Override
    public Map<Integer, ChangeNotice> parse(String s, Locale locale) throws ParseException {
        Map<Integer, ChangeNotice> resultMap = new HashMap<>();
        String s1 = s.replaceAll("\\{", "").replaceAll("}", "");
        String[] split = s1.split(", ");
        for (String tempSplit : split) {
            resultMap.put(Integer.parseInt(tempSplit.split("=")[0]), changeNoticeService.findByName(tempSplit.split("=")[1]));
        }
        return resultMap;
    }

    @Override
    public String print(Map<Integer, ChangeNotice> integerChangeNoticeMap, Locale locale) {
        String mapAsString = integerChangeNoticeMap.keySet().stream()
                .map(key -> key + "=" + integerChangeNoticeMap.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }

}

