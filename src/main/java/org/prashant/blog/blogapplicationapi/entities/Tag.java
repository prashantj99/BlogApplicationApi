package org.prashant.blog.blogapplicationapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long tagId;

    @Column(nullable = false, name = "TAG_NAME")
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;
}
