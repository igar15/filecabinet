package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DeveloperRepository;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public void create(Developer developer) {
        Assert.notNull(developer, "developer must not be null");
        developerRepository.save(developer);
    }

    @Override
    public Developer findById(int id) {
        Developer found = developerRepository.findById(id).orElse(null);
        return ValidationUtil.checkNotFoundWithId(found, id);
    }

    public Developer findByName(String name) {
        Developer found = developerRepository.findByName(name).orElse(null);
        return ValidationUtil.checkNotFound(found, name);
    }

    @Override
    public List<Developer> findAll() {
        return developerRepository.findAll();
    }

    @Override
    public void update(Developer developer) {
        Assert.notNull(developer, "developer must not be null");
        ValidationUtil.checkNotFoundWithId(developerRepository.save(developer), developer.id());
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Developer found = developerRepository.findById(id).orElse(null);
        ValidationUtil.checkNotFoundWithId(found, id);
        List<Document> documents = documentRepository.findAllByDeveloperId(id);
        documents.forEach(document -> document.setDeveloper(null));
        developerRepository.deleteById(id);
    }
}
