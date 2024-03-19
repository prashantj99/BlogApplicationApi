package org.prashant.blog.blogapplicationapi.repository;

import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.prashant.blog.blogapplicationapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
