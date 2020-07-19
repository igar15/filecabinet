package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.ChangeNotice;

import java.util.List;

public interface ChangeNoticeService {

    void create(ChangeNotice changeNotice);

    List<ChangeNotice> findAll();

    ChangeNotice findById(int id);

    ChangeNotice findByName(String name);

    void update(ChangeNotice changeNotice);

    void deleteById(int id);

}
