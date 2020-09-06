package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ElectronicImageDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ElectronicImageDocumentRepository extends JpaRepository<ElectronicImageDocument, Integer> {

    @Query("select e from ElectronicImageDocument e where e.nonAnnulled=:nonAnnulled and e.document.id=:documentId")
    Optional<ElectronicImageDocument> findByNonAnnulledAndByDocumentId(@Param("nonAnnulled") boolean nonAnnulled, @Param("documentId") int documentId);

    @Query("select e from ElectronicImageDocument e left join fetch e.electronicImageData where e.nonAnnulled=:nonAnnulled and e.document.id=:documentId")
    Optional<ElectronicImageDocument> findByDocumentIdAndNonAnnulledWithElectronicImageData(@Param("nonAnnulled") boolean nonAnnulled, @Param("documentId") int documentId);

    @Modifying
    @Transactional
    @Query("delete from ElectronicImageDocument e where e.document.id=:documentId and e.id=:id")
    void delete(@Param("documentId") int documentId, @Param("id") int id);


    @Query("select e from ElectronicImageDocument e join fetch e.electronicImageData where e.document.id=:documentId and e.id=:id")
    Optional<ElectronicImageDocument> findByIdAndDocumentIdWithElectronicImageData(@Param("documentId") int documentId, @Param("id") int id);

    @Query("select e from ElectronicImageDocument e where e.document.id=:documentId and e.id=:id")
    Optional<ElectronicImageDocument> findByIdAndDocumentId(@Param("documentId") int documentId, @Param("id") int id);

    @Transactional
    @Modifying
    @Query("update ElectronicImageDocument e set e.nonAnnulled=:nonAnnulled where e.document.id=:documentId and e.id=:id")
    void annull(@Param("documentId") int documentId, @Param("id") int id, @Param("nonAnnulled") boolean nonAnnulled);

    Optional<ElectronicImageDocument> findByChangeNumberAndDocument_Id(int changeNumber, int documentId);

    List<ElectronicImageDocument> findAllByNonAnnulledAndDocument_Id(boolean nonAnnulled, int documentId);
}
