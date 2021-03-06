package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @EntityGraph(attributePaths = {"department", "originalHolder"})
    Optional<Document> findByDecimalNumber(String decimalNumber);

    @EntityGraph(attributePaths = {"department", "originalHolder"})
    @Query("select d from Document d left join fetch d.changeNotices c left join fetch c.department  where d.id=:id")
    Optional<Document> findByIdWithChangeNotices(@Param("id") int id);

    @Query("select d from Document d left join fetch d.changeNotices where d.decimalNumber=:decimalNumber")
    Optional<Document> findByDecimalNumberWithChangeNotices(@Param("decimalNumber") String decimalNumber);

    @Query("select d from Document d left join fetch d.department left join fetch d.originalHolder left join fetch d.externalDispatches where d.id=:id")
    Optional<Document> findByIdWithExternalDispatches(@Param("id") int id);

    @Query("select d from Document d left join fetch d.department left join fetch d.originalHolder left join fetch d.internalDispatches where d.id=:id")
    Optional<Document> findByIdWithInternalDispatches(@Param("id") int id);

//    @EntityGraph(attributePaths = {"department", "originalHolder"})
    @Query("select d from Document d left join fetch d.department left join fetch d.originalHolder left join fetch d.applicabilities a left join fetch a.department left join fetch a.originalHolder where d.id=:id")
    Optional<Document> findByIdWithApplicabilities(@Param("id") int id);

    List<Document> findAllByApplicabilities_DecimalNumber(String decimalNumber);

    @EntityGraph(attributePaths = {"department", "originalHolder"})
    Page<Document> findAll(Example example, Pageable pageable);

    @EntityGraph(attributePaths = {"department", "originalHolder"})
    Page<Document> findAllByReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    @EntityGraph(attributePaths = {"department", "originalHolder"})
    Page<Document> findAllByDepartment_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String departmentName,
                                                                                                    LocalDate after, LocalDate before, Pageable pageable);
    @EntityGraph(attributePaths = {"department", "originalHolder"})
    Page<Document> findAllByOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String originalHolderName,
                                                                                                         LocalDate after, LocalDate before, Pageable pageable);
    @EntityGraph(attributePaths = {"department", "originalHolder"})
    Page<Document> findAllByDepartment_NameAndOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String departmentName, String originalHolderName,
                                                                                                                          LocalDate after, LocalDate before, Pageable pageable);
    @Transactional
    @Modifying
    @Query("update Document d set d.name=:name, d.decimalNumber =:decimalNumber, d.inventoryNumber=:inventoryNumber, " +
            "d.receiptDate=:receiptDate, d.status=:status, d.form=:form, d.stage=:stage, d.sheetsAmount=:sheetsAmount, " +
            "d.format=:format, d.a4Amount=:a4Amount, d.department=:department, d.originalHolder=:originalHolder where d.id=:id")
    void updateWithoutChildren(@Param("id") int id, @Param("name") String name, @Param("decimalNumber") String decimalNumber, @Param("inventoryNumber") int inventoryNumber,
                               @Param("receiptDate") LocalDate receiptDate, @Param("status") Status status, @Param("form") Form form,
                               @Param("stage") Stage stage, @Param("sheetsAmount") int sheetsAmount, @Param("format") String format,
                               @Param("a4Amount") int a4Amount, @Param("department") Department department, @Param("originalHolder") Company originalHolder);


    @EntityGraph(attributePaths = {"department", "originalHolder"})
    @Query("select d from Document d left join fetch d.electronicImageDocuments e where d.id=:id")
    Optional<Document> findByIdWithElectronicImage(@Param("id") int id);


}
