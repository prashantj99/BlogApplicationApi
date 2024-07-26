package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.CreateTagRequest;
import org.prashant.blog.blogapplicationapi.payload.TagDT;
import org.prashant.blog.blogapplicationapi.payload.TagResponse;
import org.prashant.blog.blogapplicationapi.repository.PostRepository;
import org.prashant.blog.blogapplicationapi.repository.TagRepository;
import org.prashant.blog.blogapplicationapi.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.prashant.blog.blogapplicationapi.payload.TagDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public TagDT createTag(CreateTagRequest createTagRequest) {
        Tag tag = new Tag();
        tag.setName(createTagRequest.tagName());
        Tag savedTag = tagRepository.save(tag);
        return new TagDT(savedTag);
    }

    @Override
    public TagResponse getAllTags(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Tag> page_tags = tagRepository.findAll(pageable);
        List<TagDT> tags = page_tags.getContent().stream().map(TagDT::new).toList();
        return new TagResponse(tags,
                page_tags.getNumber(),
                page_tags.getSize(),
                page_tags.getTotalElements(),
                page_tags.getTotalPages(), page_tags.isLast());
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(()-> new ResourceNotFound("Tag", "tagId", tagId.toString()));
        tagRepository.delete(tag);
    }

    @Override
    public void removeTagsByPost(Long postId) {
        Post post =  postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFound("Post", "postId", postId.toString()));
        post.getTags().clear();
        postRepository.save(post);
    }
}
