package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Company;
import java.util.List;

public interface CompanyService {

    Company create(Company company);

    Company findById(int id);

    Company findByName(String name);

    List<Company> findAll();

    void update(Company company);

    void deleteById(int id);
}
