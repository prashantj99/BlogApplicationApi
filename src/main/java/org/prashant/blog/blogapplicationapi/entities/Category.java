package org.prashant.blog.blogapplicationapi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "description")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "category_subscribers",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> subscribers;
}
