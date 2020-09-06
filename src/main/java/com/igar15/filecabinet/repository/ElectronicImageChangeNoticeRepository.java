package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ElectronicImageChangeNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ElectronicImageChangeNoticeRepository extends JpaRepository<ElectronicImageChangeNotice, Integer> {

    Optional<ElectronicImageChangeNotice> findByChangeNotice_Id(int changeNoticeId);

    Optional<ElectronicImageChangeNotice> findByIdAndAndChangeNotice_Id(int id, int changeNoticeId);

    @Query("select e from ElectronicImageChangeNotice e join fetch e.electronicImageData where e.changeNotice.id=:changeNoticeId")
    Optional<ElectronicImageChangeNotice> findByChangeNoticeIdWithElectronicImageData(@Param("changeNoticeId") int changeNoticeId);

    @Modifying
    @Transactional
    @Query("delete from ElectronicImageChangeNotice e where e.changeNotice.id=:changeNoticeId and e.id=:id")
    void delete(@Param("changeNoticeId") int changeNoticeId, @Param("id") int id);



}
