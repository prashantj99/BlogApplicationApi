package org.prashant.blog.blogapplicationapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpid;

    @Column(nullable = false)
    private  Long otp;

    @Column(nullable = false)
    String token;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;
}
