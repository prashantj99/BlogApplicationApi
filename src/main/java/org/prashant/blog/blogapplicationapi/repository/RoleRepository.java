package org.prashant.blog.blogapplicationapi.repository;


import org.prashant.blog.blogapplicationapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
