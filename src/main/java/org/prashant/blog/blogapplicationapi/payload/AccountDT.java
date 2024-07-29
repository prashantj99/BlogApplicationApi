package org.prashant.blog.blogapplicationapi.payload;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.prashant.blog.blogapplicationapi.entities.Account;
import org.prashant.blog.blogapplicationapi.entities.User;

public record AccountDT(
        Long accountId,
        String link,
        String platform
) {
}
