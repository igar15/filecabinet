package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.InternalDispatch;

import java.util.List;

public interface InternalDispatchService {

    InternalDispatch create(InternalDispatch internalDispatch);

    InternalDispatch findById(int id);

    List<InternalDispatch> findAll();

    List<InternalDispatch> findAllByDocumentId(int documentId);

    void update(InternalDispatch internalDispatch);

    void deleteById(int id);

}
