package org.prashant.blog.blogapplicationapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private Long id;

    @Column(nullable = false, name = "ROLE_NAME")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    // Constructors, getters, setters, and other methods
}
