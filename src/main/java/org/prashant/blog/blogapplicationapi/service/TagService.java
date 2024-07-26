package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.CreateTagRequest;
import org.prashant.blog.blogapplicationapi.payload.TagDT;
import org.prashant.blog.blogapplicationapi.payload.TagDto;
import org.prashant.blog.blogapplicationapi.payload.TagResponse;
import org.springframework.data.domain.Page;

public interface TagService {
    TagDT createTag(CreateTagRequest createTagRequest);
    TagResponse getAllTags(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteTag(Long tagId);
    void removeTagsByPost(Long postId);
}
