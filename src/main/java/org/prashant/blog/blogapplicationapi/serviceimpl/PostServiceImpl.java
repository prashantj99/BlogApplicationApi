package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.repository.CategoryRepository;
import org.prashant.blog.blogapplicationapi.repository.PostRepository;
import org.prashant.blog.blogapplicationapi.repository.TagRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.FileService;
import org.prashant.blog.blogapplicationapi.service.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Value("${project.baseurl}")
    private String baseurl;

    @Override
    public PostDto createPost(CreatePostRequest createPostRequest, Long userId) throws IOException {
        System.out.println(createPostRequest);
        Category category = this.categoryRepository.findById(createPostRequest.categoryId())
                .orElseThrow(()->new ResourceNotFound("Category", "categoryId", createPostRequest.categoryId().toString()));
        User user = this.userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFound("User", "userId", userId.toString()));
        Post post = new Post();
        post.setPostTitle(createPostRequest.postTitle());
        post.setPostContent(createPostRequest.postContent());
        post.setPostDescription(createPostRequest.postDescription());
        post.setDraft(createPostRequest.draft());
        post.setAddedDate(new Date());
        post.setLastUpdateDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        //save tags
        List<Tag> tags=null;
        if(!createPostRequest.tags().isEmpty()){
            tags = createPostRequest.tags().stream()
            .map(tagName -> tagRepository.findByTagName(tagName.toLowerCase())
            .orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setTagName(tagName.toLowerCase());
                return tagRepository.save(newTag);
            })).toList();
        }
        post.setTags(tags);
        post.setImageName(createPostRequest.bannerUrl());
        Post saved_post = this.postRepository.save(post);
        return this.modelMapper.map(saved_post, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostUpdateRequest postUpdateRequest) throws IOException {
        Post post = this.postRepository.findById(postUpdateRequest.postId())
                .orElseThrow(() -> new ResourceNotFound("Post", "postId", postUpdateRequest.postId().toString()));

        User user = userRepository.findById(postUpdateRequest.userId())
                .orElseThrow(() -> new ResourceNotFound("User", "userId", postUpdateRequest.userId().toString()));

        Category category = this.categoryRepository.findById(postUpdateRequest.categoryId())
                .orElseThrow(()->new ResourceNotFound("Category", "categoryId", postUpdateRequest.categoryId().toString()));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You are not authorized to update this post.");
        }

        //if the post is still a draft
        if(postUpdateRequest.draft()) {
            if (postUpdateRequest.postTitle().isBlank()) {
                throw new RuntimeException("post title is empty!!!");
            }
            if (postUpdateRequest.postContent().isBlank()) {
                throw new RuntimeException("post content is empty!!!");
            }
            if (postUpdateRequest.tags().isEmpty()) throw new RuntimeException("at least one tag is required!!!");
            if (postUpdateRequest.bannerUrl().isBlank()) throw new RuntimeException("banner is required!!!");
        }
        if(!postUpdateRequest.draft()){
            if (postUpdateRequest.postContent().length() >= 3) {
                throw new RuntimeException("post content is must be 3 character long!!!");
            }
            if(postUpdateRequest.postDescription().isBlank()){
                throw new RuntimeException("add a small description to your post!!!");
            }
            post.setPostDescription(postUpdateRequest.postDescription());
        }

        // Fetch and set tags
        List<Tag> tags = new ArrayList<>();
        for (String tagName : postUpdateRequest.tags()) {
            Tag tag = this.tagRepository.findByTagName(tagName)
                    .orElseGet(() -> {
                        Tag created_tag = new Tag();
                        created_tag.setTagName(tagName);
                        return created_tag;
                    }); // If tag does not exist, create a new one
            tags.add(tag);
        }
        post.setTags(tags);
        post.setPostTitle(postUpdateRequest.postTitle());
        post.setPostContent(postUpdateRequest.postContent());
        post.setImageName(postUpdateRequest.bannerUrl());
        post.setCategory(category);
        post.setLastUpdateDate(new Date());
        post.setDraft(postUpdateRequest.draft());

        Post updated_post = this.postRepository.save(post);
        return modelMapper.map(updated_post, PostDto.class);
    }

    @Override
    public void deletePost(DeletePostRequest deletePostRequest) throws IOException {
        Post post = this.postRepository.findById(deletePostRequest.postId())
                .orElseThrow(()-> new ResourceNotFound("Post", "postId", deletePostRequest.postId().toString()));

        User user = userRepository.findById(deletePostRequest.userId())
                .orElseThrow(() -> new ResourceNotFound("User", "userId", deletePostRequest.userId().toString()));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You are not authorized to update this post.");
        }

        this.fileService.deleteResource(path, post.getImageName());
        this.postRepository.delete(post);
    }

    @Override
    public PostResponse getPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> page_post = this.postRepository.findAll(pageable);
        List<PostDto> all_posts = page_post.getContent().stream().map( post -> this.modelMapper.map(post, PostDto.class)).toList();
        return new PostResponse(all_posts,
                    page_post.getNumber(),
                page_post.getSize(),
                page_post.getTotalElements(),
                page_post.getTotalPages(), page_post.isLast());
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFound("Post", "postId", postId.toString()));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostResponse getPostsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category", "categoryId", categoryId.toString()));

        Page<Post> postsPage = postRepository.findByCategory(category, pageable);

        List<PostDto> postDtos = postsPage.getContent().stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .toList();

        return new PostResponse(
                postDtos,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }

    @Override
    public PostResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.toString()));

        Page<Post> postsPage = postRepository.findByUser(user, pageable);

        List<PostDto> postDtos = postsPage.getContent().stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .toList();

        return new PostResponse(
                postDtos,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }

    @Override
    public PostResponse getPostsByTag(String tagName, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Retrieve the tag from the repository
        Tag tag = this.tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new ResourceNotFound("Tag", "tagName", tagName));

        // Fetch posts associated with the tag using the repository method
        Page<Post> postsPage = postRepository.findByTagsContaining(tag, pageable);

        // Convert Page<Post> to a list of PostDto
        List<PostDto> postDtos = postsPage.getContent().stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .toList();

        // Create and return a PostResponse object containing the paginated post data
        return new PostResponse(
                postDtos,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }

    @Override
    public PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> postsPage = postRepository.findByPostTitleContainingIgnoreCaseOrPostContentContainingIgnoreCase(keyword, pageable);

        List<PostDto> postDtos = postsPage.getContent().stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .toList();

        return new PostResponse(
                postDtos,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }
}
