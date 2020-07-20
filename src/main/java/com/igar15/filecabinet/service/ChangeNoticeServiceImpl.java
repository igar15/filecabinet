package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ChangeNoticeServiceImpl implements ChangeNoticeService {

    @Autowired
    ChangeNoticeRepository changeNoticeRepository;

    @Override
    public ChangeNotice create(ChangeNotice changeNotice) {
        Assert.notNull(changeNotice, "changeNotice must not be null");
        return changeNoticeRepository.save(changeNotice);
    }

    @Override
    public List<ChangeNotice> findAll() {
        return changeNoticeRepository.findAll();
    }

    @Override
    public ChangeNotice findById(int id) {
        ChangeNotice found = changeNoticeRepository.findById(id).orElse(null);
        return ValidationUtil.checkNotFoundWithId(found, id);
    }

    @Override
    public void update(ChangeNotice changeNotice) {
        Assert.notNull(changeNotice, "changeNotice must not be null");
        ValidationUtil.checkNotFoundWithId(changeNoticeRepository.save(changeNotice), changeNotice.id());
    }

    @Override
    public void deleteById(int id) {
        ChangeNotice found = changeNoticeRepository.findById(id).orElse(null);
        ValidationUtil.checkNotFoundWithId(found, id);
        changeNoticeRepository.deleteById(id);
    }

    @Override
    public ChangeNotice findByName(String name) {
        ChangeNotice found = changeNoticeRepository.findByName(name).orElse(null);
        return ValidationUtil.checkNotFound(found, name);
    }
}
