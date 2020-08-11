package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    Department create(Department department);

    Department findById(int id);

    Department findByName(String name);

    List<Department> findAll();

    Page<Department> findAll(Pageable pageable);

    List<Department> findAllByCanTakeAlbums(boolean canTakeAlbums);

    List<Department> findAllByIsDeveloper(boolean isDeveloper);

    void update(Department department);

    void deleteById(int id);

}
