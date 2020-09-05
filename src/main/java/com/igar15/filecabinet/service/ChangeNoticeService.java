package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.ChangeNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChangeNoticeService {

    ChangeNotice create(ChangeNotice changeNotice);

    ChangeNotice findById(int id);

    ChangeNotice findByIdWithDocuments(int id);

    ChangeNotice findByName(String name);

    Page<ChangeNotice> findAll(String name, String department, String changeCode, String after, String before, Pageable pageable);

    void update(ChangeNotice changeNotice);

    void updateWithoutChildren(ChangeNotice changeNotice);

    Object[] addDocument(ChangeNotice changeNotice, String newDocument, String newDocumentChangeNumber);

    String removeDocument(ChangeNotice changeNotice, int documentId);

    void deleteById(int id);

    ChangeNotice findByIdWithElectronicImage(int id);
}
