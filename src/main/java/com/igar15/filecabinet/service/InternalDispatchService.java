package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.InternalDispatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InternalDispatchService {

    InternalDispatch create(InternalDispatch internalDispatch);

    InternalDispatch findById(int id);

    InternalDispatch findByIdWithDocuments(int id);

    InternalDispatch findByIdAndIsAlbumWithDocuments(int internalId, boolean b);

    Page<InternalDispatch> findAll(Pageable pageable);

    Page<InternalDispatch> findAllByIsAlbumAndIsActive(boolean isAlbum, boolean isActive, Pageable pageable);

    Page<InternalDispatch> findAllByAlbumNameContainsIgnoreCaseAndIsActive(String albumName, boolean isAlbum, Pageable pageable);

    void update(InternalDispatch internalDispatch);

    void updateWithoutChildren(InternalDispatch internalDispatch);

    String addDocument(InternalDispatch internalDispatch, String newDocument);

    String removeDocument(InternalDispatch internalDispatch, int documentId);

    void deleteById(int id);
}
