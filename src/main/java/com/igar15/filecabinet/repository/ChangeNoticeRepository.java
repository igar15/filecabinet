package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.ChangeNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangeNoticeRepository extends JpaRepository<ChangeNotice, Integer> {

    Optional<ChangeNotice> findByName(String name);

}
