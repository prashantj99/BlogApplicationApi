package org.prashant.blog.blogapplicationapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Setter
@Getter
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(length = 200, name = "description")
    private String description;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "draft")
    private Boolean draft;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "published")
    private Date published;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    private Date lastUpdated;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Activity> postActivities;

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Tag> tags;

    @Column(name = "views", nullable = false)
    private int views = 0; // Default value is 0

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;
}
