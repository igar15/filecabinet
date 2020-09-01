package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = "department")
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "department")
    Page<User> findAllByEmail(String email, Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Optional<User> findById(int id);

    @Transactional
    @Modifying
    @Query("update User u set u.nonLocked=:nonLocked where u.id=:id")
    void changeStatus(@Param("nonLocked") boolean nonLocked, @Param("id") int id);

    @Transactional
    @Modifying
    @Query("update User u set u.name=:name, u.email=:email, u.department=:department, u.role=:role where u.id=:id")
    void updateUserWithoutPassword(@Param("name") String name, @Param("email") String email, @Param("department") Department department, @Param("role") Role role, @Param("id") int id);

    @Transactional
    @Modifying
    @Query("update User u set u.password=:password where u.email=:email")
    void updateUserPassword(@Param("password") String password, @Param("email") String email);

    List<User> findAllByNonLocked(boolean nonLocked);
}
