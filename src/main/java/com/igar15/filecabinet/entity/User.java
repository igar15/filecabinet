package com.igar15.filecabinet.entity;


import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.util.validation.PasswordMatches;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@PasswordMatches
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @Email
    @NotEmpty(message = "Email is required.")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password is required.")
    @Column(name = "password")
    private String password;

    @Transient
    @NotBlank(message = "Password confirmation is required.")
    private String passwordConfirmation;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
