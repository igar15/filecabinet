package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.PasswordResetToken;
import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.repository.PasswordResetTokenRepository;
import com.igar15.filecabinet.repository.UserRepository;
import com.igar15.filecabinet.service.UserService;
import com.igar15.filecabinet.util.exception.EmailExistsException;
import com.igar15.filecabinet.util.exception.NotFoundException;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //


    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findAllByNonLocked(boolean nonLocked) {
        return userRepository.findAllByNonLocked(nonLocked);
    }

    @Override
    public Page<User> findAllByEmail(String email, Pageable pageable) {
        return userRepository.findAllByEmail(email, pageable);
    }

    @Override
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(final String email) {
        if (email == null) {
            throw new NotFoundException("User with null email not found");
        }
        return ValidationUtil.checkNotFound(userRepository.findByEmail(email).orElse(null), email);
    }

    @Override
    public User findById(int id) {
        return ValidationUtil.checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    @Override
    public void deleteById(int id) {
        ValidationUtil.checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        userRepository.deleteById(id);
    }

    @Override
    public void changeStatus(User user) {
        userRepository.changeStatus(user.getNonLocked(), user.getId());
    }

    @Override
    public void updateWithoutPassword(User user) {
        userRepository.updateUserWithoutPassword(user.getName(), user.getEmail(), user.getDepartment(), user.getRole(), user.getId());
    }

    @Override
    public void updateUserPassword(String email, String password) {
        userRepository.updateUserPassword(passwordEncoder.encode(password), email);
    }

    @Override
    public Page<User> findAllActive(Pageable pageable, String email) {
        Page<User> page = null;

        final List<Object> principals = sessionRegistry.getAllPrincipals();
        final org.springframework.security.core.userdetails.User[] users = principals.toArray(new org.springframework.security.core.userdetails.User[principals.size()]);

        List<User> activeUsers = Arrays.stream(users)
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(u -> userRepository.findByEmail(u.getUsername()).orElse(null))
                .collect(Collectors.toList());

        if (email == null) {
            page = new PageImpl<>(activeUsers, pageable, pageable.getOffset());
        }
        else {
            List<User> activeUsersByEmail = activeUsers.stream()
                    .filter(user -> user.getEmail().equals(email))
                    .collect(Collectors.toList());
            page = new PageImpl<>(activeUsersByEmail, pageable, pageable.getOffset());
        }
        return page;
    }













    @Override
    public User registerNewUser(final User user) throws EmailExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }


    @Override
    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    //

    private boolean emailExist(final String email) {
        final User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public User updateExistingUser(User user) throws EmailExistsException {
        final Integer id = user.getId();
        final String email = user.getEmail();
        final User emailOwner = userRepository.findByEmail(email).orElse(null);

        if (emailOwner != null && !id.equals(emailOwner.getId())) {
            throw new EmailExistsException("Email not available.");
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
}
