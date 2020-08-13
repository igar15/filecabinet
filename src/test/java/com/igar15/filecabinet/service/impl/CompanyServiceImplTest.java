package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Company;
import com.igar15.filecabinet.service.CompanyService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.igar15.filecabinet.CompanyTestData.*;

class CompanyServiceImplTest extends AbstractServiceTest {

    @Autowired
    private CompanyService companyService;

    @Test
    void create() {
        Company created = companyService.create(getNew());
        int newId = created.id();
        Company newCompany = getNew();
        newCompany.setId(newId);
        Assertions.assertEquals(newCompany, created);
        Assertions.assertEquals(companyService.findById(newId), newCompany);
    }

    @Test
    void createDuplicateNameAndAddress() {
        Assertions.assertThrows(DataAccessException.class, () -> companyService.create(getNewWithDuplicateNameAndAddress()));
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(company -> {
            validateRootCause(PSQLException.class, () -> companyService.create(company));
        });
    }

    @Test
    void findById() {
        Company found = companyService.findById(COMPANY1_ID);
        Assertions.assertEquals(COMPANY1, found);
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> companyService.findById(NOT_FOUND_ID));
    }

    @Test
    void findByName() {
        Company found = companyService.findByName(COMPANY1_NAME);
        Assertions.assertEquals(COMPANY1, found);
    }

    @Test
    void findByNameNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> companyService.findByName(NOT_FOUND_NAME));
    }

    @Test
    void findAll() {
        List<Company> companies = companyService.findAll();
        Assertions.assertEquals(3, companies.size());
    }

    @Test
    void findAllWithPageable() {
        Page<Company> page = companyService.findAll(PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_ALL, page);
    }

    @Test
    void findAllByNameContainsIgnoreCase() {
        Page<Company> page = companyService.findAllByNameContainsIgnoreCase("ao", PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_ALL_BY_NAME, page);
    }

    @Test
    void update() {
        Company updated = getUpdated();
        companyService.update(updated);
        Assertions.assertEquals(updated, companyService.findById(COMPANY1_ID));
    }

    @Test
    void deleteById() {
        companyService.deleteById(COMPANY1_ID + 1);
        Assertions.assertThrows(NotFoundException.class, () -> companyService.findById(COMPANY1_ID + 1));
    }

    @Test
    void deleteByIdWithExistReferences() {
        Assertions.assertThrows(DataAccessException.class, () -> companyService.deleteById(COMPANY1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> companyService.deleteById(NOT_FOUND_ID));
    }
}