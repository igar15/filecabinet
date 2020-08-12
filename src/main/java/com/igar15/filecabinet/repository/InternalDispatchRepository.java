package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Query("select i from InternalDispatch i left join fetch i.documents where i.id=:id")
    Optional<InternalDispatch> findByIdWithDocuments(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("update InternalDispatch i set i.waybill=:waybill, i.dispatchDate=:dispatchDate, i.status=:status, i.remark=:remark, " +
            "i.stamp=:stamp, i.dispatchHandler=:dispatchHandler, i.receivedInternalDate=:receivedInternalDate, " +
            "i.internalHandlerName=:internalHandlerName, i.internalHandlerPhoneNumber=:internalHandlerPhoneNumber, " +
            "i.isAlbum=:isAlbum, i.albumName=:albumName, i.isActive=:isActive where i.id=:id")
    void updateWithoutChildren(@Param("id") int id, @Param("waybill") String waybill, @Param("dispatchDate") LocalDate dispatchDate,
                               @Param("status") Status status, @Param("remark") String remark, @Param("stamp") Stamp stamp,
                               @Param("dispatchHandler") Department dispatchHandler, @Param("receivedInternalDate") LocalDate receivedInternalDate,
                               @Param("internalHandlerName") String internalHandlerName, @Param("internalHandlerPhoneNumber")  String internalHandlerPhoneNumber,
                               @Param("isAlbum") Boolean isAlbum, @Param("albumName") String albumName, @Param("isActive") boolean isActive);
}
