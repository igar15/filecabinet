package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.HelperUtil;
import com.igar15.filecabinet.util.exception.NotFoundException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChangeNoticeServiceImpl implements ChangeNoticeService {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    DocumentService documentService;

    @Autowired
    ChangeNoticeRepository changeNoticeRepository;

    @Override
    public ChangeNotice create(ChangeNotice changeNotice) {
        Assert.notNull(changeNotice, "changeNotice must not be null");
        return changeNoticeRepository.save(changeNotice);
    }

    @Override
    public void updateWithoutChildren(ChangeNotice changeNotice) {
        changeNoticeRepository.updateWithoutChildren(changeNotice.getId(), changeNotice.getName(), changeNotice.getChangeCode(),
                changeNotice.getIssueDate(), changeNotice.getDepartment());
    }

    @Override
    public ChangeNotice findById(int id) {
        return ValidationUtil.checkNotFoundWithId(changeNoticeRepository.findById(id).orElse(null), id);
    }

    @Override
    public Long countDocumentsById(int id) {
        return changeNoticeRepository.countDocumentsById(id);
    }

    @Override
    public ChangeNotice findByIdWithDocuments(int id) {
        return ValidationUtil.checkNotFoundWithId(changeNoticeRepository.findByIdWithDocuments(id).orElse(null), id);
    }

    @Override
    public ChangeNotice findByName(String name) {
//        Assert.notNull(name, "Change notice name must not be null");
        if (name == null) {
            throw new NotFoundException("Change notice not found");
        }
        return ValidationUtil.checkNotFound(changeNoticeRepository.findByName(name).orElse(null), name);
    }

    @Override
    public Page<ChangeNotice> findAll(String name, String department, String changeCode, String after, String before, Pageable pageable) {
        LocalDate afterDate = (after == null) ? LocalDate.of(1900, 1, 1) : LocalDate.parse(after);
        LocalDate beforeDate = (before == null) ? LocalDate.of(2050, 1, 1) : LocalDate.parse(before);

        Page<ChangeNotice> changeNotices = null;

        Integer changeCodeInt = null;
        try {
            changeCodeInt = (changeCode == null) ? null : Integer.valueOf(changeCode);
        } catch (NumberFormatException e) {
        }

        if (HelperUtil.checkParamsOnNull(name, department, changeCode)) {
            changeNotices = changeNoticeRepository.findAllByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(afterDate, beforeDate, pageable);
        }
        else if (HelperUtil.checkParamsOnNull(name, department)) {
            changeNotices = changeNoticeRepository.findAllByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(changeCodeInt, afterDate, beforeDate, pageable);
        } else if (HelperUtil.checkParamsOnNull(name, changeCode)) {
            changeNotices = changeNoticeRepository.findAllByDepartment_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(department, afterDate, beforeDate, pageable);
        } else if (HelperUtil.checkParamsOnNull(name)) {
            changeNotices = changeNoticeRepository.findAllByDepartment_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(department, changeCodeInt, afterDate, beforeDate, pageable);
        }
        else {
            ChangeNotice changeNotice = new ChangeNotice();
            changeNotice.setName(name);
            if (department != null) {
                changeNotice.setDepartment(departmentService.findByName(department));
            }
            changeNotice.setChangeCode(changeCodeInt);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
            Example<ChangeNotice> example = Example.of(changeNotice, matcher);
            changeNotices = changeNoticeRepository.findAll(example, pageable);
            if (after != null || before != null) {
                List<ChangeNotice> collect = changeNotices.get()
                        .filter(change -> (change.getIssueDate().compareTo(afterDate) >= 0) && (change.getIssueDate().compareTo(beforeDate) <= 0))
                        .collect(Collectors.toList());
                changeNotices = new PageImpl<>(collect, pageable, pageable.getOffset());
            }
        }
        return changeNotices;
    }

    @Override
    public void update(ChangeNotice changeNotice) {
        Assert.notNull(changeNotice, "changeNotice must not be null");
        ValidationUtil.checkNotFoundWithId(changeNoticeRepository.save(changeNotice), changeNotice.id());
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(changeNoticeRepository.findById(id).orElse(null), id);
        changeNoticeRepository.deleteById(id);
    }

    @Override
    public Object[] addDocument(ChangeNotice changeNotice, String newDocument, String newDocumentChangeNumber) {
        String docErrorMessage = null;
        String numberErrorMessage = null;

        if (newDocument == null) {
            docErrorMessage = "Decimal number must not be empty";
            if (newDocumentChangeNumber == null) {
                numberErrorMessage = "Change number must not be empty";
            }
        }
        else if (newDocumentChangeNumber == null) {
            numberErrorMessage = "Change number must not be empty";
            if (newDocument == null) {
                docErrorMessage = "Decimal number must not be empty";
            }
        }
        else {
            Document document = null;
            try {
                document = documentService.findByDecimalNumberWithChangeNotices(newDocument);
            } catch (NotFoundException e) {
            }
            if (document == null) {
                docErrorMessage = "Document does not exist";
            }
            else if (!changeNotice.isNew() && changeNotice.getDocuments().containsKey(document)) {
                docErrorMessage = "This change notice already has this document";
            }
            else {
                Integer changeNumber = null;
                try {
                    changeNumber = Integer.valueOf(newDocumentChangeNumber);
                    if (changeNumber < 1) {
                        numberErrorMessage = "Change number must be greater than 0";
                    }
                    else if (document.getChangeNotices().containsKey(changeNumber)){
                        numberErrorMessage = "Document already has this change number";
                    }
                    else {
                        if (changeNotice.isNew()) {
                            changeNotice.setDocuments(new HashMap<>());
                        }
                        changeNotice.getDocuments().put(document, changeNumber);
                        changeNoticeRepository.save(changeNotice);
                    }
                } catch (NumberFormatException e) {
                    numberErrorMessage = "Invalid change number";
                }
            }
        }
        return new Object[]{docErrorMessage, numberErrorMessage};
    }
}
