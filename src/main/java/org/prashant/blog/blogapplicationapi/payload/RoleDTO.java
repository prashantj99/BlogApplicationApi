package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Role;

public record RoleDTO(
        Long id,
        String name
) {
    public RoleDTO(Role role){
        this(role.getId(), role.getName());
    }
}
