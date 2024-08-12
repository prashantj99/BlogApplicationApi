package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.CreateTagRequest;
import org.prashant.blog.blogapplicationapi.payload.TagDTO;
import org.prashant.blog.blogapplicationapi.payload.TagResponse;

public interface TagService {
    TagDTO createTag(CreateTagRequest createTagRequest);
    TagResponse getAllTags(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteTag(Long tagId);
    void removeTagsByPost(Long postId);
}
