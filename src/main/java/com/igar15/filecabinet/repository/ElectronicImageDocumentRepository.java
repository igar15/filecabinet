package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ElectronicImageDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ElectronicImageDocumentRepository extends JpaRepository<ElectronicImageDocument, Integer> {

    @Query("select e from ElectronicImageDocument e where e.nonAnnulled=:nonAnnulled and e.document.id=:documentId")
    Optional<ElectronicImageDocument> findByNonAnnulledAndByDocumentId(@Param("nonAnnulled") boolean nonAnnulled, @Param("documentId") int documentId);

    @Modifying
    @Transactional
    @Query("delete from ElectronicImageDocument e where e.document.id=:documentId and e.id=:id")
    void delete(@Param("documentId") int documentId, @Param("id") int id);


    @Query("select e from ElectronicImageDocument e join fetch e.electronicImageData where e.document.id=:documentId and e.id=:id")
    Optional<ElectronicImageDocument> findByIdAndDocumentIdWithElectronicImageData(@Param("documentId") int documentId, @Param("id") int id);

    @Query("select e from ElectronicImageDocument e where e.document.id=:documentId and e.id=:id")
    Optional<ElectronicImageDocument> findByIdAndDocumentId(int documentId, int id);
}
