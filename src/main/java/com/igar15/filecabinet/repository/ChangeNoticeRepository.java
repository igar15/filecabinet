package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ChangeNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChangeNoticeRepository extends JpaRepository<ChangeNotice, Integer> {

    Optional<ChangeNotice> findByName(String name);

    @Query("select c from ChangeNotice c where c.developer.id=:id")
    List<ChangeNotice> findAllByDeveloperId(@Param("id") int id);

}
