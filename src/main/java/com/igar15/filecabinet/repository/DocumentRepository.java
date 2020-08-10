package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByDecimalNumber(String decimalNumber);

    @Query("select d from Document d where d.developer.id=:developerId")
    List<Document> findAllByDeveloperId(@Param("developerId") int developerId);

    List<Document> findByApplicabilities_DecimalNumber(String decimalNumber);

    List<Document> findAllByDeveloper_Name(String name);

    List<Document> findAllByOriginalHolderId(int originalHolderId);

    @Query("select d from Document d left join d.changeNotices where d.id=:id")
    Document findByIdWithChangeNotices(@Param("id") int id);

    Page<Document> findAllByReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    Page<Document> findAllByDeveloper_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String developerName,
                                                                                                    LocalDate after, LocalDate before, Pageable pageable);

    Page<Document> findAllByOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String originalHolderName,
                                                                                                         LocalDate after, LocalDate before, Pageable pageable);

    Page<Document> findAllByDeveloper_NameAndOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String developerName, String originalHolderName,
                                                                                                                          LocalDate after, LocalDate before, Pageable pageable);
}
