package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
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

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Transactional
    @Modifying
    @Query("update Document d set d.name=:name, d.decimalNumber =:decimalNumber, d.inventoryNumber=:inventoryNumber, " +
            "d.receiptDate=:receiptDate, d.status=:status, d.form=:form, d.stage=:stage, d.sheetsAmount=:sheetsAmount, " +
            "d.format=:format, d.a4Amount=:a4Amount, d.department=:department, d.originalHolder=:originalHolder where d.id=:id")
    void updateDocument(@Param("id") int id, @Param("name") String name, @Param("decimalNumber") String decimalNumber, @Param("inventoryNumber") int inventoryNumber,
                        @Param("receiptDate") LocalDate receiptDate, @Param("status") Status status, @Param("form") Form form,
                        @Param("stage") Stage stage, @Param("sheetsAmount") int sheetsAmount, @Param("format") String format,
                        @Param("a4Amount") int a4Amount, @Param("department") Department department, @Param("originalHolder") Company originalHolder);

    Optional<Document> findByDecimalNumber(String decimalNumber);

    @Query("select d from Document d where d.department.id=:departmentId")
    List<Document> findAllByDepartmentId(@Param("departmentId") int departmentId);

    List<Document> findByApplicabilities_DecimalNumber(String decimalNumber);

    List<Document> findAllByDepartment_Name(String name);

    List<Document> findAllByOriginalHolderId(int originalHolderId);

    @Query("select d from Document d left join d.changeNotices where d.id=:id")
    Document findByIdWithChangeNotices(@Param("id") int id);

    Page<Document> findAllByReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    Page<Document> findAllByDepartment_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String departmentName,
                                                                                                    LocalDate after, LocalDate before, Pageable pageable);

    Page<Document> findAllByOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String originalHolderName,
                                                                                                         LocalDate after, LocalDate before, Pageable pageable);

    Page<Document> findAllByDepartment_NameAndOriginalHolder_NameAndReceiptDateGreaterThanEqualAndReceiptDateLessThanEqual(String departmentName, String originalHolderName,
                                                                                                                          LocalDate after, LocalDate before, Pageable pageable);
}
