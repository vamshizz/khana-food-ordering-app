package com.app.UserManagmentService.Service;


import com.app.UserManagmentService.Config.JwtUtil;
import com.app.UserManagmentService.Entity.Refresh;
import com.app.UserManagmentService.Repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Refresh createRefreshToken(String username) {
        //refreshTokenRepository.deleteByUsername(username);

        // generate new refresh token
        String token = jwtUtil.generateRefreshToken(username);

        Refresh refreshToken = new Refresh();
        refreshToken.setToken(token);
        refreshToken.setUsername(username);
        refreshToken.setExpiryDate(new Date(
                System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)); // 7 days
        refreshToken.setRevoked(false);

        log.info(refreshToken.getToken());

        Refresh saved= refreshTokenRepository.save(refreshToken);
        log.info("Saved refresh id={} token={}", saved.getId(), saved.getToken());

        return refreshToken;
    }
}
