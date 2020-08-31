package com.igar15.filecabinet.entity;


import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.abstracts.AbstractNamedEntity;
import com.igar15.filecabinet.entity.enums.Role;
import com.igar15.filecabinet.util.validation.EmailNotDuplicate;
import com.igar15.filecabinet.util.validation.PasswordMatches;
import com.igar15.filecabinet.util.validation.ValidPassword;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EmailNotDuplicate
@PasswordMatches
@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity {

    @Email
    @NotBlank(message = "Email name is required.")
    @Column(name = "email")
    private String email;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ValidPassword
    @NotBlank(message = "Password is required.")
    @Column(name = "password")
    private String password;

    @Transient
    @NotBlank(message = "Password confirmation is required.")
    private String passwordConfirmation;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "non_locked")
    private Boolean nonLocked;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User() {
        super();
        enabled = true;
        nonLocked = true;
    }

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean getNonLocked() {
        return nonLocked;
    }

    public void setNonLocked(Boolean nonLocked) {
        this.nonLocked = nonLocked;
    }
}
