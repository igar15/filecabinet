package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.InternalDispatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InternalDispatchService {

    InternalDispatch create(InternalDispatch internalDispatch);

    InternalDispatch findById(int id);

    InternalDispatch findByIdAndIsAlbum(int id, boolean isAlbum);

    Page<InternalDispatch> findAll(Pageable pageable);

    Page<InternalDispatch> findAllByIsAlbumAndIsActive(boolean isAlbum, boolean isActive, Pageable pageable);

    void update(InternalDispatch internalDispatch);

    void deleteById(int id);

    Page<InternalDispatch> findAllByAlbumNameContainsIgnoreCaseAndIsActive(String albumName, boolean isAlbum, Pageable pageable);
}
