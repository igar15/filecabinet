package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public ChangeNotice findById(int id) {
        return ValidationUtil.checkNotFoundWithId(changeNoticeRepository.findById(id).orElse(null), id);
    }

    @Override
    public ChangeNotice findByName(String name) {
        return changeNoticeRepository.findByName(name);
//        return ValidationUtil.checkNotFound(changeNoticeRepository.findByName(name), name);
    }

    @Override
    public List<ChangeNotice> findAll() {
        return changeNoticeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public void update(ChangeNotice changeNotice) {
        Assert.notNull(changeNotice, "changeNotice must not be null");
        ValidationUtil.checkNotFoundWithId(changeNoticeRepository.save(changeNotice), changeNotice.id());
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(changeNoticeRepository.findById(id).orElse(null), id);
        changeNoticeRepository.deleteById(id);
    }
}
