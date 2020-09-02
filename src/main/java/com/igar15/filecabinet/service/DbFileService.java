package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.DbFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DbFileService {

    DbFile create(MultipartFile file, int documentId);

    DbFile findById(int documentId, int id);

    List<DbFile> findAllByDocumentId(int documentId);

    void delete(int documentId, int id);
}
