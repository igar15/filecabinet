package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.repository.ExternalDispatchRepository;
import com.igar15.filecabinet.service.ExternalDispatchService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ExternalDispatchServiceImpl implements ExternalDispatchService {

    @Autowired
    private ExternalDispatchRepository externalDispatchRepository;

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
    public List<ExternalDispatch> findAll() {
        return externalDispatchRepository.findAll(Sort.by(Sort.Order.desc("dispatchDate")));
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
}
