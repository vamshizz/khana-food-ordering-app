package com.app.UserManagmentService.DTO;

import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
public class SignupDTO {


    private String email;
    private String password;

}