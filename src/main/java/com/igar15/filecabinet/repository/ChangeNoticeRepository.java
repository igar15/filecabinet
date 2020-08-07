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

    Optional<ChangeNotice> findByName(String name);

    @Query("select c from ChangeNotice c where c.developer.id=:id")
    List<ChangeNotice> findAllByDeveloperId(@Param("id") int id);

    Page<ChangeNotice> findByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findByDeveloper_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String developer, LocalDate after, LocalDate before, Pageable pageable);

    Page<ChangeNotice> findByDeveloper_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String developer, Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

}
