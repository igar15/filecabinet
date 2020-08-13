package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.*;
import com.igar15.filecabinet.util.HelperUtil;
import com.igar15.filecabinet.util.exception.NotFoundException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    CompanyService companyService;

    @Autowired
    ExternalDispatchService externalDispatchService;

    @Autowired
    InternalDispatchService internalDispatchService;

    @Override
    public void updateWithoutChildren(Document document) {
        Assert.notNull(document, "document must not be null");
        if (document.getSheetsAmount() == null) {
            document.setSheetsAmount(0);
        }
        if (document.getA4Amount() == null) {
            document.setA4Amount(0);
        }
        documentRepository.updateWithoutChildren(document.getId(), document.getName(), document.getDecimalNumber(), document.getInventoryNumber(),
                document.getReceiptDate(), document.getStatus(), document.getForm(), document.getStage(), document.getSheetsAmount(),
                document.getFormat(), document.getA4Amount(), document.getDepartment(), document.getOriginalHolder());
    }

    @Override
    public Document create(Document document) {
        Assert.notNull(document, "document must not be null");
        return documentRepository.save(document);
    }

    @Override
    public Document findById(int id) {
        return ValidationUtil.checkNotFoundWithId(documentRepository.findById(id).orElse(null), id);
    }

    @Override
    public Document findByIdWithChangeNotices(int id) {
        return ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithChangeNotices(id).orElse(null), id);
    }

    @Override
    public Document findByIdWithExternalDispatches(int id) {
        Document document =  ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithExternalDispatches(id).orElse(null), id);
        LinkedHashMap<ExternalDispatch, Boolean> sortedMap = document.getExternalDispatches().entrySet().stream()
                .sorted(Map.Entry.<ExternalDispatch, Boolean>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        document.setExternalDispatches(sortedMap);
        return document;
    }


    @Override
    public Document findByIdWithInternalDispatches(int id) {
        Document document = ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithInternalDispatches(id).orElse(null), id);
        LinkedHashMap<InternalDispatch, Boolean> sortedMap = document.getInternalDispatches().entrySet().stream()
                .sorted(Map.Entry.<InternalDispatch, Boolean>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        document.setInternalDispatches(sortedMap);
        return document;
    }

    @Override
    public Document findByIdWithApplicabilities(int id) {
        return ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithApplicabilities(id).orElse(null), id);
    }

    @Override
    public Document findByDecimalNumberWithChangeNotices(String newDocument) {
        return ValidationUtil.checkNotFoundWithProperty(documentRepository.findByDecimalNumberWithChangeNotices(newDocument).orElse(null), newDocument);
    }

    @Override
    public Document findByDecimalNumber(String decimalNumber) {
//        Assert.notNull(decimalNumber, "decimalNumber must not be null");
        if (decimalNumber == null) {
            throw new NotFoundException("Document not found");
        }
        return ValidationUtil.checkNotFound(documentRepository.findByDecimalNumber(decimalNumber).orElse(null), decimalNumber);
    }

    @Override
    public Page<Document> findAll(String decimalNumber, String name, String department, String originalHolder, String inventoryNumber,
                                  String status, String stage, String form, String after, String before, Pageable pageable) {

        LocalDate afterDate = (after == null) ? LocalDate.of(1900, 1, 1) : LocalDate.parse(after);
        LocalDate beforeDate = (before == null) ? LocalDate.of(2050, 1, 1) : LocalDate.parse(before);

        Page<Document> documents = null;
        if (HelperUtil.checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form, department, originalHolder)) {
            documents = documentRepository.findAllByReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(afterDate, beforeDate, pageable);
        }
        else if(HelperUtil.checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form, department)) {
            documents = documentRepository.findAllByOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(originalHolder, afterDate, beforeDate, pageable);
        }
        else if(HelperUtil.checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form, originalHolder)) {
            documents = documentRepository.findAllByDepartment_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(department, afterDate, beforeDate, pageable);
        }
        else if(HelperUtil.checkParamsOnNull(decimalNumber, name, inventoryNumber, status, stage, form)) {
            documents = documentRepository.findAllByDepartment_NameAndOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(department, originalHolder, afterDate, beforeDate, pageable);
        }
        else {
            Document document = new Document();
            Integer inventoryNumberInt = null;
            try {
                inventoryNumberInt = inventoryNumber == null ? null : Integer.valueOf(inventoryNumber);
            } catch (NumberFormatException e) {
            }
            Status docStatus = status == null ? null : Status.valueOf(status);
            Stage docStage = stage == null ? null : Stage.valueOf(stage);
            Form docForm = form == null ? null : Form.valueOf(form);
            document.setDecimalNumber(decimalNumber);
            document.setName(name);
            if (department != null) {
                document.setDepartment(departmentService.findByName(department));
            }
            if (originalHolder != null) {
                document.setOriginalHolder(companyService.findByName(originalHolder));
            }
            document.setInventoryNumber(inventoryNumberInt);
            document.setStatus(docStatus);
            document.setStage(docStage);
            document.setForm(docForm);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("decimalNumber", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
            Example<Document> example = Example.of(document, matcher);
            documents = documentRepository.findAll(example, pageable);
            if (after != null || before != null) {
                List<Document> collect = documents.get()
                        .filter(doc -> (doc.getReceiptDate().compareTo(afterDate) >= 0) && (doc.getReceiptDate().compareTo(beforeDate) <= 0))
                        .collect(Collectors.toList());
                documents = new PageImpl<>(collect, pageable, pageable.getOffset());
            }
        }
        return documents;
    }

    @Override
    public List<Document> findAllByApplicabilities_DecimalNumber(String decimalNumber) {
        return documentRepository.findAllByApplicabilities_DecimalNumber(decimalNumber);
    }

    @Override
    public void update(Document document) {
        Assert.notNull(document, "document must not be null");
        ValidationUtil.checkNotFoundWithId(documentRepository.save(document), document.id());
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(documentRepository.findById(id).orElse(null), id);
        documentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Document deregisterExternalWithIncomings(int id, int externalId) {
        Document mainDocument = ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithExternalDispatches(id).orElse(null), id);
        ExternalDispatch mainExternal = externalDispatchService.findById(externalId);

        mainDocument.getExternalDispatches().keySet()
                .forEach(externalDispatch -> {
                    if (externalDispatch.getId().equals(externalId)) {
                        mainDocument.getExternalDispatches().put(externalDispatch, false);
                    }
                });
        documentRepository.save(mainDocument);

        List<Document> documentsInMainDocument = documentRepository.findAllByApplicabilities_DecimalNumber(mainDocument.getDecimalNumber());
        Queue<Document> documents = new LinkedList<>(documentsInMainDocument);
        while (!documents.isEmpty()) {
            Document tempDocument = documents.remove();
            AtomicBoolean needDeregister = new AtomicBoolean(true);

            Set<Document> tempDocumentApplicabilities = tempDocument.getApplicabilities();
            tempDocumentApplicabilities.forEach(document -> {
                Map<ExternalDispatch, Boolean> externalDispatches = document.getExternalDispatches();
                externalDispatches.entrySet().forEach(entry -> {
                    if (entry.getValue()) {
                        if (entry.getKey().getCompany().getName().equals(mainExternal.getCompany().getName())) {
                            needDeregister.set(false);
                        }
                    }
                });
            });

            if (needDeregister.get()) {
                tempDocument.getExternalDispatches().keySet()
                        .forEach(externalDispatch -> {
                            if (externalDispatch.getCompany().getName().equals(mainExternal.getCompany().getName())) {
                                tempDocument.getExternalDispatches().put(externalDispatch, false);
                            }
                        });
                documentRepository.save(tempDocument);
                documents.addAll(documentRepository.findAllByApplicabilities_DecimalNumber(tempDocument.getDecimalNumber()));
            }
        }
        return mainDocument;
    }

    @Override
    public String addApplicability(Document document, String newApplicability) {
        String errorMessage = null;
        if (newApplicability == null) {
            errorMessage = "Decimal number must not be empty";
        }
        else {
            try {
                Document applicability = ValidationUtil.checkNotFound(documentRepository.findByDecimalNumber(newApplicability).orElse(null), newApplicability);
                if (document.getApplicabilities().contains(applicability)) {
                    errorMessage = "Applicability already added";
                }
                else {
                    document.getApplicabilities().add(applicability);
                    documentRepository.save(document);
                }

            } catch (NotFoundException e) {
                errorMessage = "Document not found";
            }
        }
        return errorMessage;
    }

    @Override
    public Document removeApplicability(int id, int applicabilityId) {
        Document document = ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithApplicabilities(id).orElse(null), id);
        document.setApplicabilities(document.getApplicabilities().stream()
                .filter(doc -> doc.getId() != applicabilityId)
                .collect(Collectors.toSet()));
        documentRepository.save(document);
        return document;
    }

    @Override
    public String removeChange(Document document, int changeId) {
        String errorMessage = null;
        long changeNoticeDocumentsSize = changeNoticeService.countDocumentsById(changeId);
        if (changeNoticeDocumentsSize == 1) {
            errorMessage = "Change notice can not exist without any documents!";
        }
        else {
            Optional<Map.Entry<Integer, ChangeNotice>> found = document.getChangeNotices().entrySet().stream()
                    .filter(entry -> entry.getValue().getId().equals(changeId))
                    .findFirst();
            if (found.isPresent()) {
                document.getChangeNotices().remove(found.get().getKey());
                documentRepository.save(document);
            }
        }
        return errorMessage;
    }

    @Override
    public Document deregisterExternal(int id, int externalId) {
        Document document = ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithExternalDispatches(id).orElse(null), id);
        document.getExternalDispatches().keySet()
                .forEach(externalDispatch -> {
                    if (externalDispatch.getId().equals(externalId)) {
                        document.getExternalDispatches().put(externalDispatch, false);
                    }
                });
        documentRepository.save(document);
        return document;
    }

    @Override
    public Document deregisterInternal(int id, int internalId) {
        Document document = ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithInternalDispatches(id).orElse(null), id);
        document.getInternalDispatches().keySet()
                .forEach(internalDispatch -> {
                    if (internalDispatch.getId().equals(internalId)) {
                        document.getInternalDispatches().put(internalDispatch, false);
                    }
                });
        documentRepository.save(document);
        return document;
    }

    @Override
    public void deregisterAlbum(int id, int internalId) {
        InternalDispatch internalDispatch = internalDispatchService.findByIdAndIsAlbumWithDocuments(internalId, true);
        Document document = ValidationUtil.checkNotFoundWithId(documentRepository.findById(id).orElse(null), id);
        if (internalDispatch.getAlbumName().equals(document.getDecimalNumber())) {
            internalDispatch.setIsActive(false);
            internalDispatch.getDocuments().keySet()
                    .forEach(key -> internalDispatch.getDocuments().put(key, false));
            internalDispatchService.update(internalDispatch);
        }
        else {
            throw new IllegalArgumentException("Can not deregister album! Album name does not equal document decimal number!");
        }
    }
}
