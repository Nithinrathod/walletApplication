package com.tcs.authService.repository;

import com.tcs.authService.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    
 // Find user by their UUID string to retrieve details
    Optional<User> findByUserId(String userId);
}
