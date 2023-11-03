package com.devrezaur.user.service.repository;

import com.devrezaur.user.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing user entities in the 'user-service-db'.
 *
 * @author Rezaur Rahman
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Retrieves a user by their unique userId.
     *
     * @param userId the unique userId to search for.
     * @return user entity if found. Or return null if not found.
     */
    User findByUserId(UUID userId);

    /**
     * Retrieves a user by their unique email.
     *
     * @param email the unique email to search for.
     * @return user entity if found. Or return null if not found.
     */
    User findByEmail(String email);

    /**
     * Retrieves a list of users by their unique userIds.
     *
     * @param userIds list of unique userIds to search for.
     * @return list of user entities that match the provided userIds.
     */
    List<User> findByUserIdIn(List<UUID> userIds);
}
