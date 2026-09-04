package com.app.UserManagmentService.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "refresh")
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "username")
    private String username;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "revoked")
    private boolean revoked;
}

