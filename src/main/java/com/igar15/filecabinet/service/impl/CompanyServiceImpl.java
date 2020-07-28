package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.CompanyRepository;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.CompanyService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public void create(Company company) {
        Assert.notNull(company, "company must not be null");
        companyRepository.save(company);
    }

    @Override
    public Company findById(int id) {
        return ValidationUtil.checkNotFoundWithId(companyRepository.findById(id).orElse(null), id);
    }

    @Override
    public Company findByName(String name) {
        Assert.notNull(name, "name must not be null");
        return ValidationUtil.checkNotFound(companyRepository.findByName(name).orElse(null), name);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public void update(Company company) {
        Assert.notNull(company, "company must not be null");
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(companyRepository.findById(id).orElse(null), id);
        List<Document> documents = documentRepository.findAllByOriginalHolderId(id);
        documents.forEach(document -> document.setOriginalHolder(null));
        companyRepository.deleteById(id);
    }
}
