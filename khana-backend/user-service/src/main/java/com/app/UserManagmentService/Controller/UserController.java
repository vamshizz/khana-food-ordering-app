package com.app.UserManagmentService.Controller;


import com.app.UserManagmentService.Config.JwtUtil;
import com.app.UserManagmentService.DTO.LoginDTO;
import com.app.UserManagmentService.DTO.LoginResponseDTO;
import com.app.UserManagmentService.DTO.SignupDTO;
import com.app.UserManagmentService.Entity.User;
import com.app.UserManagmentService.Repository.RefreshTokenRepository;
import com.app.UserManagmentService.Repository.UserRepository;
import com.app.UserManagmentService.Service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {
    @Autowired
private UserRepository userRepository;



    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;


    @GetMapping("/health")
    public ResponseEntity<String> checkUserService(){
        return  ResponseEntity.ok("Request Reached user-service");
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<Map<String,String>> registeruser(@Valid @RequestBody SignupDTO signupDTO){

        User user=new User();
        if(userRepository.findByEmail(signupDTO.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        user.setEmail(signupDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(signupDTO.getPassword()));

        User savedUser = userRepository.save(user);


        return  ResponseEntity.ok(Map.of("message", "New User Registered SuccessFully"));
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginuser(@RequestBody LoginDTO loginDto) {
LoginResponseDTO loginResponseDTO=new LoginResponseDTO();
        System.out.println(loginDto);
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        if (user.isPresent()) {
            if (bCryptPasswordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
                String token = jwtUtil.generateToken(user.get().getEmail());
                String refreshToken= userService.createRefreshToken(user.get().getEmail()).getToken();

                   loginResponseDTO.setEmail(user.get().getEmail());
                   loginResponseDTO.setToken(token);
                   loginResponseDTO.setRefresh_token(refreshToken);
                System.out.println(loginResponseDTO);
                return ResponseEntity.ok(loginResponseDTO);
            }
        } else {
            return ResponseEntity.badRequest().body(new LoginResponseDTO());
        }


        return null;
    }

    @GetMapping("/user")
    public ResponseEntity<String> user() {

        return ResponseEntity.ok("successfull");

    }




    @PostMapping("/refresh")
    public Map<String,String> refresh(@RequestBody Map<String,String> request){
        String refreshToken = request.get("refreshToken");

        jwtUtil.validateRefreshToken(refreshToken);

       String username=
               refreshTokenRepository.findByToken(refreshToken)
                       .orElseThrow(() ->
                               new RuntimeException("Refresh token not found"))
                .getUsername();

        User user = userRepository.findByEmail(username)
                .orElseThrow();

        // 4. Generate new access token
        String newAccessToken = jwtUtil.generateToken(
                user.getEmail()
        );
        return Map.of("accessToken", newAccessToken);
    }
}
