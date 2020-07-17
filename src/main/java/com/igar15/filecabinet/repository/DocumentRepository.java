package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByDecimalNumber(String decimalNumber);

    @Query("select d from Document d where d.developer.id=:developerId")
    List<Document> findAllByDeveloperId(@Param("developerId") int developerId);

}
