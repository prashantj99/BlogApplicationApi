package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Tag;

public record TagDTO(
    Long id,
    String name
) {
    public TagDTO(Tag tag){
        this(tag.getId(), tag.getName());
    }
}
