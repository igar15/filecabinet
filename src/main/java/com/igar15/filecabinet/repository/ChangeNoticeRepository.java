package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.Department;
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

public interface ChangeNoticeRepository extends JpaRepository<ChangeNotice, Integer> {

    @Query("select c from ChangeNotice c left join fetch c.documents where c.id=:id")
    Optional<ChangeNotice> findByIdWithDocuments(@Param("id") int id);

    Optional<ChangeNotice> findByName(String name);

    List<ChangeNotice> findAllByDepartment_Id(int departmentId);

    @EntityGraph(attributePaths = "department")
    Page<ChangeNotice> findAll(Example example, Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Page<ChangeNotice> findAllByIssueDateGreaterThanEqualAndIssueDateLessThanEqual(LocalDate after, LocalDate before, Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Page<ChangeNotice> findAllByChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Page<ChangeNotice> findAllByDepartment_NameAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, LocalDate after, LocalDate before, Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Page<ChangeNotice> findAllByDepartment_NameAndChangeCodeAndIssueDateGreaterThanEqualAndIssueDateLessThanEqual(String department, Integer changeCode, LocalDate after, LocalDate before, Pageable pageable);

    @Query("select size(c.documents) from ChangeNotice c where c.id=:id")
    Long countDocumentsById(int id);

    @Transactional
    @Modifying
    @Query("update ChangeNotice c set c.name=:name, c.changeCode =:changeCode, c.issueDate=:issueDate, " +
            "c.department=:department where c.id=:id")
    void updateWithoutChildren(@Param("id") int id, @Param("name") String name, @Param("changeCode") int changeCode,
                               @Param("issueDate") LocalDate issueDate, @Param("department") Department department);

}
