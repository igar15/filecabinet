package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.ChangeNoticeRepository;
import com.igar15.filecabinet.repository.DepartmentRepository;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ChangeNoticeRepository changeNoticeRepository;

    @Autowired
    InternalDispatchRepository internalDispatchRepository;

    @Override
    public Department create(Department department) {
        Assert.notNull(department, "department must not be null");
        return departmentRepository.save(department);
    }

    @Override
    public Department findById(int id) {
        return ValidationUtil.checkNotFoundWithId(departmentRepository.findById(id).orElse(null), id);
    }

    public Department findByName(String name) {
//        Assert.notNull(name, "name must not be null");
//        return ValidationUtil.checkNotFound(departmentRepository.findByName(name).orElse(null), name);
        return departmentRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Department> findByCanTakeAlbums(Boolean canTakeAlbums) {
        Assert.notNull(canTakeAlbums, "can take albums must be not null");
        return departmentRepository.findByCanTakeAlbums(canTakeAlbums);
    }

    @Override
    public void update(Department department) {
        Assert.notNull(department, "department must not be null");
        ValidationUtil.checkNotFoundWithId(departmentRepository.save(department), department.id());
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Department found = departmentRepository.findById(id).orElse(null);
        ValidationUtil.checkNotFoundWithId(found, id);
        List<Document> documents = documentRepository.findAllByDepartmentId(id);
        List<ChangeNotice> changeNotices = changeNoticeRepository.findAllByDepartmentId(id);
        documents.forEach(document -> document.setDepartment(null));
        changeNotices.forEach(changeNotice -> changeNotice.setDepartment(null));
        departmentRepository.deleteById(id);
    }
}
