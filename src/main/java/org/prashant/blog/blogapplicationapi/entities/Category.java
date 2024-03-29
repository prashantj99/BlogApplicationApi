package org.prashant.blog.blogapplicationapi.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(nullable = false, name = "CATEGORY_TITLE")
    private String categoryTitle;

    @Column(nullable = false, length = 500, name = "CATEGORY_DESC")
    private String categoryDescription;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> posts;
}
