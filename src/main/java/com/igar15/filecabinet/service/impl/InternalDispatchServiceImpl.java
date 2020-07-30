package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import com.igar15.filecabinet.service.InternalDispatchService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class InternalDispatchServiceImpl implements InternalDispatchService {

    @Autowired
    private InternalDispatchRepository internalDispatchRepository;

    @Override
    public InternalDispatch create(InternalDispatch internalDispatch) {
        Assert.notNull(internalDispatch, "internalDispatch must not be null");
        return internalDispatchRepository.save(internalDispatch);
    }

    @Override
    public InternalDispatch findById(int id) {
        return ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findById(id).orElse(null), id);
    }

    @Override
    public List<InternalDispatch> findAll() {
        return internalDispatchRepository.findAll();
    }

    @Override
    public List<InternalDispatch> findAllByDocumentId(int documentId) {
        return internalDispatchRepository.findByDocuments_Id(documentId);
    }

    @Override
    public void update(InternalDispatch internalDispatch) {
        Assert.notNull(internalDispatch, "internalDispatch must not be null");
        ValidationUtil.checkNotFoundWithId(internalDispatchRepository.save(internalDispatch), internalDispatch.id());
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(internalDispatchRepository.findById(id).orElse(null), id);
        internalDispatchRepository.deleteById(id);
    }
}
