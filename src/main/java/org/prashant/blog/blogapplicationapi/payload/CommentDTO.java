package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Comment;

import java.util.Date;

public record CommentDT(
        Long commentId,
        String commentText,
        UserDT user,
        Date commentDate,
        Date lastEdited
        ) {
        public CommentDT(Comment comment){
                this(comment.getCommentId(), comment.getCommentText(), new UserDT(comment.getUser()), comment.getCommentDate(), comment.getLastEdited());
        }

}
