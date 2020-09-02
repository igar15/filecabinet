package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.DbFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DbFileRepository extends JpaRepository<DbFile, Integer> {

    List<DbFile> findAllByDocument_Id(int documentId);

    @Query("select dbf from DbFile dbf where dbf.document.id=:documentId and dbf.id=:id")
    Optional<DbFile> findById(@Param("documentId") int documentId, @Param("id") int id);

    @Modifying
    @Transactional
    @Query("delete from DbFile dbf where dbf.document.id=:documentId and dbf.id=:id")
    void delete(@Param("documentId") int documentId, @Param("id") int id);
}
