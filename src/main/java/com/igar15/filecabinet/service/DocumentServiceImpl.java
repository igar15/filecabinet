package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public void create(Document document) {
        Assert.notNull(document, "document must not be null");
        documentRepository.save(document);
    }

    @Override
    public void update(Document document) {
        Assert.notNull(document, "document must not be null");
        ValidationUtil.checkNotFoundWithId(documentRepository.save(document), document.id());
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document findById(int id) {
        Document found = documentRepository.findById(id).orElse(null);
        return ValidationUtil.checkNotFoundWithId(found, id);
    }

    @Override
    public List<Document> findByDecimalNumber(String decimalNumber) {
        return documentRepository.findByDecimalNumber(decimalNumber);
    }

    @Override
    public void deleteById(int id) {
        Document found = documentRepository.findById(id).orElse(null);
        ValidationUtil.checkNotFoundWithId(found, id);
        documentRepository.deleteById(id);
    }

}
