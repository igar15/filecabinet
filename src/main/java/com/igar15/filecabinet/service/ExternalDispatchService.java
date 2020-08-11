package com.igar15.filecabinet.service;


import com.igar15.filecabinet.entity.ExternalDispatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExternalDispatchService {

    ExternalDispatch create(ExternalDispatch externalDispatch);

    ExternalDispatch findById(int id);

    Page<ExternalDispatch> findAll(Pageable pageable);

    void update(ExternalDispatch externalDispatch);

    void deleteById(int id);

}
