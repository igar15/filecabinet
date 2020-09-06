package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.ElectronicImageChangeNotice;
import org.springframework.web.multipart.MultipartFile;

public interface ElectronicImageChangeNoticeService {
    ElectronicImageChangeNotice findByChangeNoticeId(int changeNoticeId);

    ElectronicImageChangeNotice create(MultipartFile file, int changeNoticeId);

    void delete(int changeNoticeId, int id);

    ElectronicImageChangeNotice findByChangeNoticeIdWithElectronicImageData(int changeNoticeId);
}
