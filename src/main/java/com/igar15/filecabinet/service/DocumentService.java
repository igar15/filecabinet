package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DocumentService {

    Document create(Document document);

    Document findById(int id);

    Document findByDecimalNumber(String decimalNumber);

    Document findByIdWithChangeNotices(int id);

    Page<Document> findAll(String decimalNumber, String name, String department, String originalHolder, String inventoryNumber,
                           String status, String stage, String form, String after, String before, Pageable pageable);

    void update(Document document);

    void deleteById(int id);

    void updateWithoutChildren(Document document);

    List<Document> findAllByApplicabilities_DecimalNumber(String decimalNumber);

    Document findByIdWithExternalDispatches(int id);

    Document findByIdWithInternalDispatches(int id);

    Document findByIdWithApplicabilities(int id);


    Document findByDecimalNumberWithChangeNotices(String newDocument);

    Document deregisterExternalWithIncomings(int id, int externalId);

    String addApplicability(Document document, String newApplicability);

    Document removeApplicability(int id, int applicabilityId);

    String removeChange(Document document, int changeId);

    Document deregisterExternal(int id, int externalId);

    Document deregisterInternal(int id, int internalId);

    void deregisterAlbum(int id, int internalId);
}
