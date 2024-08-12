package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Tag;

public record TagDT(
    Long id,
    String name
) {
    public TagDT(Tag tag){
        this(tag.getId(), tag.getName());
    }
}
