package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.ElectronicImageDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ElectronicImageDocumentService {


    ElectronicImageDocument findByNonAnnulledAndByDocumentId(boolean nonAnnulled, int documentId);

    ElectronicImageDocument create(MultipartFile file, int changeNumber, int documentId);

    void delete(int documentId, int id);

    ElectronicImageDocument findByIdAndDocumentIdWithElectronicImageData(int documentId, int id);

    void annull(int documentId, int id);

    List<ElectronicImageDocument> findAllByNonAnnulledAndDocumentId(boolean nonAnnulled, int documentId);
}


