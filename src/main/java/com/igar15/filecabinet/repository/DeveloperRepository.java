package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeveloperRepository extends JpaRepository<Developer, Integer> {

    Optional<Developer> findByName(String name);

}
