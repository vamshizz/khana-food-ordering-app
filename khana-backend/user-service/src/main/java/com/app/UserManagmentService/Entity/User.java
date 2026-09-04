package com.app.UserManagmentService.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "profile")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",unique = true, nullable = false)
    private String email;
    private String password;

}
