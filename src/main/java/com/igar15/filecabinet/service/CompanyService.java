package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CompanyService {

    Company create(Company company);

    Company findById(int id);

    Company findByName(String name);

    List<Company> findAll();

    Page<Company> findAll(Pageable pageable);

    Page<Company> findAllByNameContainsIgnoreCase(String companyName, Pageable pageable);

    void update(Company company);

    void deleteById(int id);
}
