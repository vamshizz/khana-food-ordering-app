package com.app.UserManagmentService.DTO;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String email;
    private  String token;
    private String refresh_token;
}
