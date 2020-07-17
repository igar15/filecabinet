package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Document;

import java.util.List;

public interface DocumentService {

    void create(Document document);

    Document findById(int id);

    List<Document> findByDecimalNumber(String decimalNumber);

    List<Document> findAll();

    void update(Document document);

    void deleteById(int id);

}
