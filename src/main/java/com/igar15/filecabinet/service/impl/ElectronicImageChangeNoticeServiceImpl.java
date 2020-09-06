package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.*;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.repository.ElectronicImageChangeNoticeRepository;
import com.igar15.filecabinet.service.ElectronicImageChangeNoticeService;
import com.igar15.filecabinet.util.exception.ChangeNumberDuplicateException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ElectronicImageChangeNoticeServiceImpl implements ElectronicImageChangeNoticeService {

    @Autowired
    private ElectronicImageChangeNoticeRepository electronicImageChangeNoticeRepository;

    @Autowired
    private ChangeNoticeRepository changeNoticeRepository;


    @Override
    public ElectronicImageChangeNotice findByChangeNoticeId(int changeNoticeId) {
        return electronicImageChangeNoticeRepository.findByChangeNotice_Id(changeNoticeId).orElse(null);
    }

    @Override
    public ElectronicImageChangeNotice create(MultipartFile file, int changeNoticeId) {
        ChangeNotice changeNotice = ValidationUtil.checkNotFoundWithId(changeNoticeRepository.getOne(changeNoticeId), changeNoticeId);
        String fileName = file.getOriginalFilename();
        try {
            ElectronicImageData electronicImageData = new ElectronicImageData(file.getBytes());
            ElectronicImageChangeNotice electronicImageChangeNotice = new ElectronicImageChangeNotice(fileName, file.getContentType(), electronicImageData, changeNotice);
            return electronicImageChangeNoticeRepository.save(electronicImageChangeNotice);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file");
        }
    }

    @Override
    public void delete(int changeNoticeId, int id) {
        ElectronicImageChangeNotice electronicImageChangeNotice = ValidationUtil.checkNotFound(electronicImageChangeNoticeRepository.findByIdAndAndChangeNotice_Id(id, changeNoticeId).orElse(null), "with change notice id " + changeNoticeId + " and id " + id);
        electronicImageChangeNotice.setElectronicImageData(null);
        electronicImageChangeNoticeRepository.delete(electronicImageChangeNotice);
    }

    @Override
    public ElectronicImageChangeNotice findByChangeNoticeIdWithElectronicImageData(int changeNoticeId) {
        return ValidationUtil.checkNotFound(electronicImageChangeNoticeRepository
                .findByChangeNoticeIdWithElectronicImageData(changeNoticeId).orElse(null), " with change notice id " + changeNoticeId);
    }
}
