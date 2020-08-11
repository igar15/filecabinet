package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.ChangeNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChangeNoticeService {

    ChangeNotice create(ChangeNotice changeNotice);

    Page<ChangeNotice> findAll(String name, String department, String changeCode, String after, String before, Pageable pageable);

    ChangeNotice findById(int id);

    ChangeNotice findByName(String name);

    void update(ChangeNotice changeNotice);

    void deleteById(int id);

}
