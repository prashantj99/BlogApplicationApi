package org.prashant.blog.blogapplicationapi.repository;

import org.prashant.blog.blogapplicationapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u.name FROM User u WHERE u.name LIKE %:prefix% ORDER BY u.name ASC")
    Page<String> findUserSuggestions(@Param("prefix") String prefix, Pageable pageable);
    Page<User> findAllByNameOrAbout(String name, String about, Pageable pageable);
    @Query("SELECT u.followers FROM User u WHERE u.userId = :userId")
    Optional<List<User>> findFollowersByUserId(@Param("userId") Long userId);
}
