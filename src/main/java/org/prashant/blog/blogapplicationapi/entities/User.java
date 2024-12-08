package org.prashant.blog.blogapplicationapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@RequiredArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "auth_provider")
    private String authProvider;

    @Column(name = "about")
    private String about;

    @Column(nullable = false, name = "profile_img")
    private String profileImg;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> createdPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Activity> activities;

    @ManyToMany(mappedBy = "subscribers")
    private List<Category> subscribedCategories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Account> accounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private Set<Role> roles;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;


    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followedUsers = new HashSet<>();

    @ManyToMany(mappedBy = "followedUsers")
    private Set<User> followers = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Notification> notifications = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
