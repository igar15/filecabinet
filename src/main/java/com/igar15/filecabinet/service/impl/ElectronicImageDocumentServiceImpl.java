package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ElectronicImageData;
import com.igar15.filecabinet.entity.ElectronicImageDocument;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.repository.ElectronicImageDocumentRepository;
import com.igar15.filecabinet.service.ElectronicImageDocumentService;
import com.igar15.filecabinet.util.exception.ChangeNumberDuplicateException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ElectronicImageDocumentServiceImpl implements ElectronicImageDocumentService {

    @Autowired
    private ElectronicImageDocumentRepository electronicImageDocumentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public ElectronicImageDocument findByNonAnnulledAndByDocumentId(boolean nonAnnulled, int documentId) {
        return electronicImageDocumentRepository.findByNonAnnulledAndByDocumentId(nonAnnulled, documentId).orElse(null);
    }

    @Override
    public ElectronicImageDocument create(MultipartFile file, int changeNumber, int documentId) {
        Optional<ElectronicImageDocument> eid = electronicImageDocumentRepository.findByChangeNumberAndDocument_Id(changeNumber, documentId);
        if (eid.isPresent()) {
            throw new ChangeNumberDuplicateException("Change number already exist");
        }

        Document document = ValidationUtil.checkNotFoundWithId(documentRepository.getOne(documentId), documentId);
        String fileName = file.getOriginalFilename();
        try {
            ElectronicImageData electronicImageData = new ElectronicImageData(file.getBytes());
            ElectronicImageDocument electronicImageDocument = new ElectronicImageDocument(fileName, file.getContentType(), electronicImageData, true, changeNumber, document);
            return electronicImageDocumentRepository.save(electronicImageDocument);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file");
        }
    }

    @Override
    public void delete(int documentId, int id) {
        ElectronicImageDocument electronicImageDocument = ValidationUtil.checkNotFound(electronicImageDocumentRepository.findByIdAndDocumentId(documentId, id).orElse(null), "with document id " + documentId + " and id " + id);
        electronicImageDocument.setElectronicImageData(null);
        electronicImageDocumentRepository.delete(electronicImageDocument);
    }

    @Override
    public ElectronicImageDocument findByIdAndDocumentIdWithElectronicImageData(int documentId, int id) {
        return ValidationUtil.checkNotFound(electronicImageDocumentRepository.findByIdAndDocumentIdWithElectronicImageData(documentId, id).orElse(null), "with document id " + documentId + " and id " + id);
    }

    @Override
    public ElectronicImageDocument findByDocumentIdAndNonAnnulledWithElectronicImageData(int documentId) {
        return ValidationUtil.checkNotFound(electronicImageDocumentRepository.findByDocumentIdAndNonAnnulledWithElectronicImageData(true, documentId).orElse(null), "with document id " + documentId);
    }

    @Override
    public void annull(int documentId, int id) {
        electronicImageDocumentRepository.annull(documentId, id, false);
    }

    @Override
    public List<ElectronicImageDocument> findAllByNonAnnulledAndDocumentId(boolean nonAnnulled, int documentId) {
        return electronicImageDocumentRepository.findAllByNonAnnulledAndDocument_Id(nonAnnulled, documentId);
    }
}
