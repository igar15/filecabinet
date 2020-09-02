package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.DbFile;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DbFileRepository;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DbFileService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DbFileServiceImpl implements DbFileService {

    @Autowired
    private DbFileRepository dbFileRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public List<DbFile> findAllByDocumentId(int documentId) {
        return dbFileRepository.findAllByDocument_Id(documentId);
    }

    @Override
    public void delete(int documentId, int id) {
        dbFileRepository.delete(documentId, id);
    }











    @Override
    public DbFile create(MultipartFile file, int documentId) {
        Document document = ValidationUtil.checkNotFoundWithId(documentRepository.getOne(documentId), documentId);
        String fileName = file.getOriginalFilename();
        try {
            DbFile dbFile = new DbFile(fileName, file.getContentType(), file.getBytes());
            dbFile.setDocument(document);
            return dbFileRepository.save(dbFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file");
        }
    }

    @Override
    public DbFile findById(int documentId, int id) {
        return dbFileRepository.findById(documentId, id).orElseThrow(() -> new NotFoundException("File not found"));
    }


}
