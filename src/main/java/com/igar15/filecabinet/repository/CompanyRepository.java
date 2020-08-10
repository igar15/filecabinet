package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findByName(String name);

    Page<Company> findByNameContainsIgnoreCase(String name, Pageable pageable);

}
