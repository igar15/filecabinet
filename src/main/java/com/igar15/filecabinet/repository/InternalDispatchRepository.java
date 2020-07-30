package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.InternalDispatch;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternalDispatchRepository extends JpaRepository<InternalDispatch, Integer> {

    List<InternalDispatch> findByDocuments_Id(int documentId);

    List<InternalDispatch> findByDocuments_Id(int documentId, Sort sort);

}
