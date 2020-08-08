package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document create(Document document) {
        Assert.notNull(document, "document must not be null");
        return documentRepository.save(document);
    }

    @Override
    public Document findById(int id) {
        return ValidationUtil.checkNotFoundWithId(documentRepository.findById(id).orElse(null), id);
    }

    @Override
    public Document findByIdWithChangeNotices(int id) {
        return ValidationUtil.checkNotFoundWithId(documentRepository.findByIdWithChangeNotices(id), id);
    }

    @Override
    public Document findByDecimalNumber(String decimalNumber) {
        Assert.notNull(decimalNumber, "decimalNumber must not be null");
        return ValidationUtil.checkNotFound(documentRepository.findByDecimalNumber(decimalNumber).orElse(null), decimalNumber);
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll(Sort.by(Sort.Direction.ASC, "decimalNumber"));
    }

    @Override
    public void update(Document document) {
        Assert.notNull(document, "document must not be null");
        ValidationUtil.checkNotFoundWithId(documentRepository.save(document), document.id());
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(documentRepository.findById(id).orElse(null), id);
        documentRepository.deleteById(id);
    }

}
