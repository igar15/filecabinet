package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.repository.CompanyRepository;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.CompanyService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    @CacheEvict(value = "companies", allEntries = true)
    public Company create(Company company) {
        Assert.notNull(company, "company must not be null");
        return companyRepository.save(company);
    }

    @Override
    @Cacheable("companies")
    public Company findById(int id) {
        return ValidationUtil.checkNotFoundWithId(companyRepository.findById(id).orElse(null), id);
    }

    @Override
    @Cacheable("companies")
    public Company findByName(String name) {
        Assert.notNull(name, "Company name must not be null");
        return ValidationUtil.checkNotFound(companyRepository.findByName(name).orElse(null), name);
    }

    @Override
    @Cacheable("companies")
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Page<Company> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Override
    public Page<Company> findAllByNameContainsIgnoreCase(String companyName, Pageable pageable) {
        return companyRepository.findAllByNameContainsIgnoreCase(companyName, pageable);
    }

    @Override
    @CacheEvict(value = "companies", allEntries = true)
    public void update(Company company) {
        Assert.notNull(company, "company must not be null");
        companyRepository.save(company);
    }

    @Override
    @CacheEvict(value = "companies", allEntries = true)
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(companyRepository.findById(id).orElse(null), id);
        companyRepository.deleteById(id);
    }
}
