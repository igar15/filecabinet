package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.repository.ExternalDispatchRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.ExternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ExternalDispatchServiceImpl implements ExternalDispatchService {

    @Autowired
    private ExternalDispatchRepository externalDispatchRepository;

    @Autowired
    private DocumentService documentService;

    @Override
    public ExternalDispatch create(ExternalDispatch externalDispatch) {
        Assert.notNull(externalDispatch, "externalDispatch must not be null");
        return externalDispatchRepository.save(externalDispatch);
    }

    @Override
    public ExternalDispatch findById(int id) {
        return ValidationUtil.checkNotFoundWithId(externalDispatchRepository.findById(id).orElse(null), id);
    }

    @Override
    public void updateWithoutChildren(ExternalDispatch externalDispatch) {
        Assert.notNull(externalDispatch, "external dispatch must not be null");
        externalDispatchRepository.updateWithoutChildren(externalDispatch.getId(), externalDispatch.getWaybill(),
                externalDispatch.getDispatchDate(), externalDispatch.getStatus(), externalDispatch.getRemark(),
                externalDispatch.getLetterOutgoingNumber(), externalDispatch.getCompany());
    }

    @Override
    public ExternalDispatch findByIdWithDocuments(int id) {
        return ValidationUtil.checkNotFoundWithId(externalDispatchRepository.findByIdWithDocuments(id).orElse(null), id);
    }

    @Override
    public Page<ExternalDispatch> findAll(Pageable pageable) {
        return externalDispatchRepository.findAll(pageable);
    }

    @Override
    public void update(ExternalDispatch externalDispatch) {
        Assert.notNull(externalDispatch, "externalDispatch must not be null");
        ValidationUtil.checkNotFoundWithId(externalDispatchRepository.save(externalDispatch), externalDispatch.id());
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(externalDispatchRepository.findById(id).orElse(null), id);
        externalDispatchRepository.deleteById(id);
    }

    @Override
    public String addDocument(ExternalDispatch externalDispatch, String newDocument) {
        String errorMessage = null;
        if (newDocument == null) {
            errorMessage = "Decimal number must not be empty";
        }
        else {
            try {
                Document document = documentService.findByDecimalNumber(newDocument);
                if (externalDispatch.getDocuments().containsKey(document)) {
                    errorMessage = "Document already added";
                }
                else {
                    externalDispatch.getDocuments().put(document, true);
                    externalDispatchRepository.save(externalDispatch);
                }

            } catch (NotFoundException e) {
                errorMessage = "Document not found";
            }
        }
        return errorMessage;
    }

    @Override
    public String removeDocument(ExternalDispatch externalDispatch, int documentId) {
        String errorDeleteMessage = null;
        if (externalDispatch.getDocuments().size() == 1) {
            errorDeleteMessage = "External dispatch " + externalDispatch.getWaybill() + " can not exist without any documents!";
        }
        else {
            Document found = externalDispatch.getDocuments().keySet().stream()
                    .filter(document -> document.getId().equals(documentId))
                    .findFirst().orElse(null);
            externalDispatch.getDocuments().remove(found);
            externalDispatchRepository.save(externalDispatch);
        }
        return errorDeleteMessage;
    }
}
