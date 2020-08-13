package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.Department;
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

public interface ChangeNoticeRepository extends JpaRepository<ChangeNotice, Integer> {

    @Query("select c from ChangeNotice c left join fetch c.documents where c.id=:id")
    Optional<ChangeNotice> findByIdWithDocuments(@Param("id") int id);

    Optional<ChangeNotice> findByName(String name);

    List<ChangeNotice> findAllByDepartment_Id(int departmentId);

    Page<ChangeNotice> findAllByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findAllByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findAllByDepartment_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findAllByDepartment_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

    Long countDocumentsById(int id);

    @Transactional
    @Modifying
    @Query("update ChangeNotice c set c.name=:name, c.changeCode =:changeCode, c.issueDate=:issueDate, " +
            "c.department=:department where c.id=:id")
    void updateWithoutChildren(@Param("id") int id, @Param("name") String name, @Param("changeCode") int changeCode,
                               @Param("issueDate") LocalDate issueDate, @Param("department") Department department);

}
