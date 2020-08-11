package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department create(Department department);

    Department findById(int id);

    Department findByName(String name);

    List<Department> findAll();

    List<Department> findAllByCanTakeAlbums(boolean canTakeAlbums);

    List<Department> findAllByIsDeveloper(boolean isDeveloper);

    void update(Department department);

    void deleteById(int id);


}
