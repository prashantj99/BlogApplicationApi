package org.prashant.blog.blogapplicationapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Setter
@Getter
@ToString
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
    @JsonBackReference
    private Set<User> users;

    // Constructors, getters, setters, and other methods
}
