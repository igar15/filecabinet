package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ChangeNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChangeNoticeRepository extends JpaRepository<ChangeNotice, Integer> {


    Optional<ChangeNotice> findByName(String name);

    List<ChangeNotice> findAllByDepartment_Id(int departmentId);

    Page<ChangeNotice> findAllByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findAllByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findAllByDepartment_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findAllByDepartment_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

}
