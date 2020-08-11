package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.InternalDispatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InternalDispatchRepository extends JpaRepository<InternalDispatch, Integer> {

    List<InternalDispatch> findAllByAlbumName(String albumName);

    List<InternalDispatch> findAllByDispatchHandler_Id(int dispatchHandlerId);

    Optional<InternalDispatch> findByIdAndIsAlbum(int id, boolean isAlbum);

    Page<InternalDispatch> findAllByIsAlbumAndIsActive(boolean isAlbum, boolean isActive, Pageable pageable);

    @Query("select i from InternalDispatch i join i.documents d where ?1 in (key(d))")
    Page<InternalDispatch> findAllByDocumentId(int documentId, Pageable pageable);

    Page<InternalDispatch> findAllByAlbumNameContainsIgnoreCaseAndIsActive(String albumName, boolean isActive, Pageable pageable);

}
