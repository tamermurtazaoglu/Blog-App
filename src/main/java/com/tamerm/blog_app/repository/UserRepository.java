package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return an optional containing the user if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
}