package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.PasswordResetToken;
import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.util.exception.EmailExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);

    User create(User user);

    User findById(int id);

    void deleteById(int id);

    List<User> findAllByNonLocked(boolean nonLocked);








    User registerNewUser(User user) throws EmailExistsException;

    User updateExistingUser(User user) throws EmailExistsException;


    void createPasswordResetTokenForUser(User user, String token);


    PasswordResetToken getPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    void saveRegisteredUser(User user);


    Page<User> findAllByEmail(String email, Pageable pageable);

    void changeStatus(User user);

    void updateWithoutPassword(User user);

    void updateUserPassword(int id, String password);
}
