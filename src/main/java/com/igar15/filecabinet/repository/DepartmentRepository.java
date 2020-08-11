package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByName(String name);

    List<Department> findAllByCanTakeAlbums(boolean canTakeAlbums);

    List<Department> findAllByIsDeveloper(boolean isDeveloper);

}
