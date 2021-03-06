package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.InternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;

@Service
public class InternalDispatchServiceImpl implements InternalDispatchService {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private InternalDispatchRepository internalDispatchRepository;

    @Override
    public InternalDispatch create(InternalDispatch internalDispatch) {
        Assert.notNull(internalDispatch, "internalDispatch must not be null");
        if (internalDispatch.getIsAlbum()) {
            Document document = documentService.findByDecimalNumber(internalDispatch.getAlbumName());
            internalDispatch.setDocuments(new HashMap<>());
            internalDispatch.getDocuments().put(document, true);
//            internalDispatch.setInternalHandlerName("Kochetova T.");
//            internalDispatch.setReceivedInternalDate(internalDispatch.getDispatchDate());
//            internalDispatch.setInternalHandlerPhoneNumber("1-34-15");
        }
        return internalDispatchRepository.save(internalDispatch);
    }

    @Override
    public InternalDispatch findById(int id) {
        return ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findById(id).orElse(null), id);
    }

    @Override
    public InternalDispatch findByIdWithDocuments(int id) {
        return ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findByIdWithDocuments(id).orElse(null), id);
    }

    @Override
    public InternalDispatch findByIdAndIsAlbumWithDocuments(int internalId, boolean b) {
        return (ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findByIdAndIsAlbumWithDocuments(internalId, b).orElse(null), internalId));
    }

    @Override
    public Page<InternalDispatch> findAll(Pageable pageable) {
        return internalDispatchRepository.findAll(pageable);
    }

    @Override
    public Page<InternalDispatch> findAllByIsAlbumAndIsActive(boolean isAlbum, boolean isActive, Pageable pageable) {
        return internalDispatchRepository.findAllByIsAlbumAndIsActive(isAlbum, isActive, pageable);
    }

    @Override
    public Page<InternalDispatch> findAllByAlbumNameContainsIgnoreCaseAndIsActive(String albumName, boolean isActive, Pageable pageable) {
        return internalDispatchRepository.findAllByAlbumNameContainsIgnoreCaseAndIsActive(albumName, isActive, pageable);
    }

    @Override
    public void update(InternalDispatch internalDispatch) {
        Assert.notNull(internalDispatch, "internalDispatch must not be null");
        ValidationUtil.checkNotFoundWithId(internalDispatchRepository.save(internalDispatch), internalDispatch.id());
    }

    @Override
    public void updateWithoutChildren(InternalDispatch internalDispatch) {
        Assert.notNull(internalDispatch, "internalDispatch must not be null");
        ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findById(internalDispatch.id()), internalDispatch.id());
        internalDispatchRepository.updateWithoutChildren(internalDispatch.id(),
                internalDispatch.getWaybill(), internalDispatch.getDispatchDate(), internalDispatch.getStatus(),
                internalDispatch.getRemark(), internalDispatch.getStamp(), internalDispatch.getDispatchHandler(),
                internalDispatch.getReceivedInternalDate(), internalDispatch.getInternalHandlerName(),
                internalDispatch.getInternalHandlerPhoneNumber(), internalDispatch.getIsAlbum(), internalDispatch.getAlbumName(),
                internalDispatch.getIsActive());
    }

    @Override
    public String addDocument(InternalDispatch internalDispatch, String newDocument) {
        String errorMessage = null;
        if (newDocument == null) {
            errorMessage = "Decimal number must not be empty";
        }
        else {
            try {
                Document document = documentService.findByDecimalNumber(newDocument);
                if (internalDispatch.getDocuments().containsKey(document)) {
                    errorMessage = "Document already added";
                }
                else {
                    internalDispatch.getDocuments().put(document, true);
                    internalDispatchRepository.save(internalDispatch);
                }

            } catch (NotFoundException e) {
                errorMessage = "Document not found";
            }
        }
        return errorMessage;
    }

    @Override
    public String removeDocument(InternalDispatch internalDispatch, int documentId) {
        String errorDeleteMessage = null;
        if (internalDispatch.getDocuments().size() == 1) {
            errorDeleteMessage = "Internal dispatch " + internalDispatch.getWaybill() + " can not exist without any documents!";
        }
        else {
            Document found = internalDispatch.getDocuments().keySet().stream()
                    .filter(document -> document.getId().equals(documentId))
                    .findFirst().orElse(null);
            internalDispatch.getDocuments().remove(found);
            internalDispatchRepository.save(internalDispatch);
        }
        return errorDeleteMessage;
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findById(id).orElse(null), id);
        internalDispatchRepository.deleteById(id);
    }
}
