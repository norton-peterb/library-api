package com.example.library.api.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_login")
public class UserLoginEntity {
    @Id
    @SequenceGenerator(name = "seqUserLogin",sequenceName = "seq_user_login",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seqUserLogin")
    private Long id;
    private String username;
    private String password;
    private String roles;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
