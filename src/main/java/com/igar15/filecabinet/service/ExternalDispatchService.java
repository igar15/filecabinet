package com.igar15.filecabinet.service;


import com.igar15.filecabinet.entity.ExternalDispatch;

import java.util.List;

public interface ExternalDispatchService {

    ExternalDispatch create(ExternalDispatch externalDispatch);

    ExternalDispatch findById(int id);

    List<ExternalDispatch> findAll();

    List<ExternalDispatch> findAllByDocumentId(int documentId);

    void update(ExternalDispatch externalDispatch);

    void deleteById(int id);

}
