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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    InternalDispatchRepository internalDispatchRepository;

    @Override
    @CacheEvict(value = "departments", allEntries = true)
    public Department create(Department department) {
        Assert.notNull(department, "department must not be null");
        return departmentRepository.save(department);
    }

    @Override
    @Cacheable("departments")
    public Department findById(int id) {
        return ValidationUtil.checkNotFoundWithId(departmentRepository.findById(id).orElse(null), id);
    }

    @Override
    @Cacheable("departments")
    public Department findByName(String name) {
        if (name == null) {
            throw new NotFoundException("Department with null name not found");
        }
        return ValidationUtil.checkNotFound(departmentRepository.findByName(name).orElse(null), name);
    }

    @Override
    @Cacheable("departments")
    public List<Department> findAll() {
        return departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    @Cacheable("departments")
    public List<Department> findAllByCanTakeAlbums(boolean canTakeAlbums) {
        return departmentRepository.findAllByCanTakeAlbums(canTakeAlbums);
    }

    @Override
    @Cacheable("departments")
    public List<Department> findAllByIsDeveloper(boolean isDeveloper) {
        return departmentRepository.findAllByIsDeveloper(isDeveloper);
    }

    @Override
    @CacheEvict(value = "departments", allEntries = true)
    public void update(Department department) {
        Assert.notNull(department, "department must not be null");
        ValidationUtil.checkNotFoundWithId(departmentRepository.save(department), department.id());
    }

    @Override
    @CacheEvict(value = "departments", allEntries = true)
    public void deleteById(int id) {
        Department found = departmentRepository.findById(id).orElse(null);
        ValidationUtil.checkNotFoundWithId(found, id);
        departmentRepository.deleteById(id);
    }
}
