package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.InternalDispatchService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;

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
    public InternalDispatch findByIdAndIsAlbum(int id, boolean isAlbum) {
        return ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findByIdAndIsAlbum(id, isAlbum).orElse(null), id);
    }

    @Override
    public InternalDispatch findByIdWithDocuments(int id) {
        return ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findByIdWithDocuments(id).orElse(null), id);
    }

    @Override
    public Page<InternalDispatch> findAll(Pageable pageable) {
        return internalDispatchRepository.findAll(pageable);
    }

    @Override
    public Page<InternalDispatch> findAllByIsAlbumAndIsActive(boolean isAlbum, boolean isActive, Pageable pageable) {
        return internalDispatchRepository.findAllByIsAlbumAndIsActive(true, true, pageable);
    }

    @Override
    public Page<InternalDispatch> findAllByAlbumNameContainsIgnoreCaseAndIsActive(String albumName, boolean isAlbum, Pageable pageable) {
        return internalDispatchRepository.findAllByAlbumNameContainsIgnoreCaseAndIsActive(albumName, isAlbum, pageable);
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
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findById(id).orElse(null), id);
        internalDispatchRepository.deleteById(id);
    }
}
