package org.prashant.blog.blogapplicationapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Setter
@Getter
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
}
