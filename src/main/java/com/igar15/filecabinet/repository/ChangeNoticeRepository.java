package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ChangeNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChangeNoticeRepository extends JpaRepository<ChangeNotice, Integer> {

//    Optional<ChangeNotice> findByName(String name);

    ChangeNotice findByName(String name);

    @Query("select c from ChangeNotice c where c.department.id=:id")
    List<ChangeNotice> findAllByDepartmentId(@Param("id") int id);

    Page<ChangeNotice> findByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findByDepartment_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findByDepartment_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

}
