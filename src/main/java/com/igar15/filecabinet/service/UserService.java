package com.igar15.filecabinet.service;

import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.util.exception.EmailExistsException;

public interface UserService {

    User registerNewUser(User user) throws EmailExistsException;

    User updateExistingUser(User user) throws EmailExistsException;

}
