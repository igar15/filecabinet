package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ExternalDispatch;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExternalDispatchRepository extends JpaRepository<ExternalDispatch, Integer> {

//    @Query("select ex from ExternalDispatch ex join ex.documents d where d.id = :id")
//    List<ExternalDispatch> findAllByDocumentId(@Param("id") int documentId);

    List<ExternalDispatch> findByDocuments_Id(int documentId);

    List<ExternalDispatch> findByDocuments_Id(int documentId, Sort sort);

}
