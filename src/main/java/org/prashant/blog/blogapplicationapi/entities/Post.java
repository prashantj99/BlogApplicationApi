package org.prashant.blog.blogapplicationapi.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "POST_TITLE")
    private String postTitle;

    @Column(length = 1000, name = "POST_CONTENT")
    private String postContent;

    @Column(length = 200, name = "POST_DESC")
    private String postDescription;

    @Column(name = "IMAGE_URL")
    private String imageName;

    @Column(name = "DRAFT")
    private Boolean draft;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PUBLISHED")
    private Date addedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    private Date lastUpdateDate;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToMany
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
