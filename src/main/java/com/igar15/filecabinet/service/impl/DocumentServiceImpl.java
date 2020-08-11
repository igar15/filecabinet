package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.CompanyService;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.HelperUtil;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    CompanyService companyService;

    @Override
    public void updateWithoutChildren(Document document) {
        Assert.notNull(document, "document must not be null");
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
    public Document findByDecimalNumber(String decimalNumber) {
        Assert.notNull(decimalNumber, "decimalNumber must not be null");
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
            document.setDepartment(departmentService.findByName(department));
            document.setOriginalHolder(companyService.findByName(originalHolder));
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

}
