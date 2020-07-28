package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.repository.DeveloperRepository;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DeveloperService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ChangeNoticeRepository changeNoticeRepository;

    @Override
    public void create(Developer developer) {
        Assert.notNull(developer, "developer must not be null");
        developerRepository.save(developer);
    }

    @Override
    public Developer findById(int id) {
        return ValidationUtil.checkNotFoundWithId(developerRepository.findById(id).orElse(null), id);
    }

    public Developer findByName(String name) {
        Assert.notNull(name, "name must not be null");
        return ValidationUtil.checkNotFound(developerRepository.findByName(name).orElse(null), name);
    }

    @Override
    public List<Developer> findAll() {
        return developerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
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
        List<ChangeNotice> changeNotices = changeNoticeRepository.findAllByDeveloperId(id);
        documents.forEach(document -> document.setDeveloper(null));
        changeNotices.forEach(changeNotice -> changeNotice.setDeveloper(null));
        developerRepository.deleteById(id);
    }
}
