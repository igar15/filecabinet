package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByDecimalNumber(String decimalNumber);

    @Query("select d from Document d where d.developer.id=:developerId")
    List<Document> findAllByDeveloperId(@Param("developerId") int developerId);

    @Query("select d from Document d left join d.changeNotices where d.id=:id")
    Document findByIdWithChangeNotices(@Param("id") int id);

}
