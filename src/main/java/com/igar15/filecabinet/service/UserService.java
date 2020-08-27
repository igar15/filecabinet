package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.PasswordResetToken;
import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.entity.VerificationToken;
import com.igar15.filecabinet.util.exception.EmailExistsException;

public interface UserService {

    User findUserByEmail(final String email);

    User registerNewUser(User user) throws EmailExistsException;

    User updateExistingUser(User user) throws EmailExistsException;

    void createVerificationTokenForUser(User user, String token);

    void createPasswordResetTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String token);

    PasswordResetToken getPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    void saveRegisteredUser(User user);



}
