package com.app.UserManagmentService.Repository;

import com.app.UserManagmentService.Entity.Refresh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends JpaRepository<Refresh,Long> {

    Optional<Refresh> findByToken(String token);

    void deleteByUsername(String username);
}
