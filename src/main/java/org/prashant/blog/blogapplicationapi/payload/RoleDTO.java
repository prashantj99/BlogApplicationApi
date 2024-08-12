package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Role;

public record RoleDT(
        Long id,
        String name
) {
    public RoleDT(Role role){
        this(role.getId(), role.getName());
    }
}
