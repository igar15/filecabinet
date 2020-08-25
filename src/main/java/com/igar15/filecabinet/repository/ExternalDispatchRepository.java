package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

public interface ExternalDispatchRepository extends JpaRepository<ExternalDispatch, Integer> {

    @Query(value = "select ex from ExternalDispatch ex left join fetch ex.company", countQuery = "select count(ex) from ExternalDispatch ex left join ex.company")
    Page<ExternalDispatch> findAllWithCompany(Pageable pageable);

    @EntityGraph(attributePaths = "company")
    @Query("select ex from ExternalDispatch ex left join fetch ex.documents where ex.id=:id")
    Optional<ExternalDispatch> findByIdWithDocuments(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("update ExternalDispatch ex set ex.waybill=:waybill, ex.dispatchDate=:dispatchDate, ex.status=:status, ex.remark=:remark, " +
            "ex.letterOutgoingNumber=:letterOutgoingNumber, ex.company=:company where ex.id=:id")
    void updateWithoutChildren(@Param("id") int id, @Param("waybill") String waybill, @Param("dispatchDate") LocalDate dispatchDate,
                               @Param("status") Status status, @Param("remark") String remark,
                               @Param("letterOutgoingNumber") String letterOutgoingNumber, @Param("company") Company company);
}
