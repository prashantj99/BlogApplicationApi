package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.InvalidParameterException;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.exceptions.UnAuthorizedOperationExcpetion;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public PostDT createPost(CreatePostRequest request, Long userId) {
        Category category = this.categoryRepository.findById(request.categoryId())
                .orElseThrow(()->new ResourceNotFound("Category", "categoryId", request.categoryId().toString()));
        User user = this.userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFound("User", "userId", userId.toString()));

        Post post = new Post();
        post.setTitle(request.postTitle());
        post.setContent(request.postContent());
        post.setDescription(request.postDescription());
        post.setDraft(request.draft());
        post.setPublished(new Date());
        post.setLastUpdated(new Date());
        post.setUser(user);
        post.setCategory(category);

        //save tags
        List<Tag> tags=null;
        if(!request.tags().isEmpty()){
            tags = request.tags().stream()
            .map(tagName -> tagRepository.findByName(tagName.toLowerCase())
            .orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setName(tagName.toLowerCase());
                return tagRepository.save(newTag);
            })).toList();
        }
        post.setTags(tags);
        post.setBannerUrl(request.bannerUrl());

        //save post to database
        Post saved_post = this.postRepository.save(post);
        return new PostDT(saved_post);
    }

    @Override
    public PostDT updatePost(PostUpdateRequest postUpdateRequest) {
        Post post = this.postRepository.findById(postUpdateRequest.postId())
                .orElseThrow(() -> new ResourceNotFound("Post", "postId", postUpdateRequest.postId().toString()));

        User user = userRepository.findById(postUpdateRequest.userId())
                .orElseThrow(() -> new ResourceNotFound("User", "userId", postUpdateRequest.userId().toString()));

        Category category = this.categoryRepository.findById(postUpdateRequest.categoryId())
                .orElseThrow(()->new ResourceNotFound("Category", "categoryId", postUpdateRequest.categoryId().toString()));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new UnAuthorizedOperationExcpetion("You are not authorized to update this post.");
        }

        //if the post is still a draft
        if(postUpdateRequest.draft()) {
            if (postUpdateRequest.postTitle().isBlank()) {
                throw new RuntimeException("post title is empty!!!");
            }
            if (postUpdateRequest.postContent().isBlank()) {
                throw new RuntimeException("post content is empty!!!");
            }
            if (postUpdateRequest.bannerUrl().isBlank()) throw new InvalidParameterException("banner is required!!!");
        }
        if(!postUpdateRequest.draft()){
            if (postUpdateRequest.postContent().isBlank()) {
                throw new InvalidParameterException("post content cannot be blank!!!");
            }
            if (postUpdateRequest.tags().isEmpty()) throw new InvalidParameterException("at least one tag is required!!!");
            if(postUpdateRequest.postDescription().isBlank()){
                throw new InvalidParameterException("add a small description to your post!!!");
            }
        }

        // Fetch and set tags
        List<Tag> tags = new ArrayList<>();
        for (String tagName : postUpdateRequest.tags()) {
            Tag tag = this.tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag created_tag = new Tag();
                        created_tag.setName(tagName);
                        return created_tag;
                    }); // If tag does not exist, create a new one
            // Save the tag if it is newly created
            if (tag.getId() == null) {
                tag = this.tagRepository.save(tag);
            }
            tags.add(tag);
        }
        post.setTags(tags);
        post.setDescription(postUpdateRequest.postDescription());
        post.setTitle(postUpdateRequest.postTitle());
        post.setContent(postUpdateRequest.postContent());
        post.setBannerUrl(postUpdateRequest.bannerUrl());
        post.setCategory(category);
        post.setLastUpdated(new Date());
        post.setDraft(postUpdateRequest.draft());

        Post updated_post = this.postRepository.save(post);
        return new PostDT(updated_post);
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

        this.fileService.deleteResource(path, post.getBannerUrl());
        this.postRepository.delete(post);
    }

    @Override
    public PostPageResponse getPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> page_post = this.postRepository.findAll(pageable);
        List<PostDT> posts = page_post.getContent().stream().map(PostDT::new).toList();
        return new PostPageResponse(posts,
                    page_post.getNumber(),
                page_post.getSize(),
                page_post.getTotalElements(),
                page_post.getTotalPages(), page_post.isLast());
    }

    @Override
    public PostDT getPostById(Long postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFound("Post", "postId", postId.toString()));
        return new PostDT(post);
    }

    @Override
    public PostPageResponse getPostsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category", "categoryId", categoryId.toString()));

        Page<Post> postsPage = postRepository.findByCategory(category, pageable);

        List<PostDT> posts = postsPage.getContent().stream().map(PostDT::new).toList();

        return new PostPageResponse(
                posts,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }

    @Override
    public PostPageResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.toString()));

        Page<Post> postsPage = postRepository.findByUser(user, pageable);

        List<PostDT> posts = postsPage.getContent().stream().map(PostDT::new).toList();

        return new PostPageResponse(
                posts,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }

    @Override
    public PostPageResponse getPostsByTag(String tagName, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Retrieve the tag from the repository
        Tag tag = this.tagRepository.findByName(tagName)
                .orElseThrow(() -> new ResourceNotFound("Tag", "tagName", tagName));

        // Fetch posts associated with the tag using the repository method
        Page<Post> postsPage = postRepository.findByTagsContaining(tag, pageable);

        // Convert Page<Post> to a list of PostDto
        List<PostDT> posts = postsPage.getContent().stream().map(PostDT::new).toList();

        // Create and return a PostResponse object containing the paginated post data
        return new PostPageResponse(
                posts,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }

    @Override
    public PostPageResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> postsPage = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, pageable);

        List<PostDT> posts = postsPage.getContent().stream().map(PostDT::new).toList();

        return new PostPageResponse(
                posts,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }
}
