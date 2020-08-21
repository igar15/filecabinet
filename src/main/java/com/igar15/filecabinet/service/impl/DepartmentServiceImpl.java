package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.repository.DepartmentRepository;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    InternalDispatchRepository internalDispatchRepository;

    @Override
    public Department create(Department department) {
        log.debug("Department Service >> Creating department {}", department);
        Assert.notNull(department, "department must not be null");
        return departmentRepository.save(department);
    }

    @Override
    public Department findById(int id) {
        log.debug("Department Service >> Finding department by Id {}", id);
        return ValidationUtil.checkNotFoundWithId(departmentRepository.findById(id).orElse(null), id);
    }

    public Department findByName(String name) {
        log.debug("Department Service >> Finding department by Name {}", name);
        if (name == null) {
            throw new NotFoundException("Department with null name not found");
        }
        return ValidationUtil.checkNotFound(departmentRepository.findByName(name).orElse(null), name);
    }

    @Override
    public List<Department> findAll() {
        log.debug("Department Service >> Finding all departments");
        return departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Page<Department> findAll(Pageable pageable) {
        log.debug("Department Service >> Finding all departments with pagination {}", pageable);
        return departmentRepository.findAll(pageable);
    }

    @Override
    public List<Department> findAllByCanTakeAlbums(boolean canTakeAlbums) {
        log.debug("Department Service >> Finding all departments by CanTakeAlbums {}", canTakeAlbums);
        return departmentRepository.findAllByCanTakeAlbums(canTakeAlbums);
    }

    @Override
    public List<Department> findAllByIsDeveloper(boolean isDeveloper) {
        log.debug("Department Service >> Finding all departments by IsDeveloper {}", isDeveloper);
        return departmentRepository.findAllByIsDeveloper(isDeveloper);
    }

    @Override
    public void update(Department department) {
        log.debug("Department Service >> Updating department {}", department);
        Assert.notNull(department, "department must not be null");
        ValidationUtil.checkNotFoundWithId(departmentRepository.save(department), department.id());
    }

    @Override
    public void deleteById(int id) {
        log.debug("Department Service >> Deleting department by Id {}", id);
        Department found = departmentRepository.findById(id).orElse(null);
        ValidationUtil.checkNotFoundWithId(found, id);
        departmentRepository.deleteById(id);
    }
}
