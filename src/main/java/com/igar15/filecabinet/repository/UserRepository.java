package com.igar15.filecabinet.repository;

import com.igar15.filecabinet.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findAllByEmail(String email, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update User u set u.enabled=:enabled where u.id=:id")
    void changeStatus(@Param("enabled") boolean enabled, @Param("id") int id);
}
