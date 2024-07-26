package org.prashant.blog.blogapplicationapi.payload;

import java.util.Date;

public record CommentDT(
        Long commentId,
        String commentText,
        UserDT user,
        Date commentDate,
        Date lastEdited
        ) {

}
