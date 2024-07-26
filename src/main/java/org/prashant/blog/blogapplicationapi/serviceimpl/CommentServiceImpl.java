package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Comment;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.repository.CommentRepository;
import org.prashant.blog.blogapplicationapi.repository.PostRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public CommentDto createComment(CreateCommentRequest createCommentRequest) {
        Post post = postRepository.findById(createCommentRequest.postId())
                .orElseThrow(()-> new ResourceNotFound("Post", "postId", createCommentRequest.postId().toString()));
        User user = userRepository.findById(createCommentRequest.userId())
                .orElseThrow(()-> new ResourceNotFound("User", "userId", createCommentRequest.userId().toString()));

        Comment comment = new Comment();
        comment.setCommentText(createCommentRequest.commentText());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCommentDate(new Date());
        comment.setLastEdited(new Date());

        Comment saved_comment = commentRepository.save(comment);
        return modelMapper.map(saved_comment, CommentDto.class);
    }

    @Override
    public CommentDto updateComment(UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new ResourceNotFound("Comment", "commentId", request.commentId().toString()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFound("User", "userId", request.userId().toString()));

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You are not authorized to update this comment.");
        }

        comment.setCommentText(request.commentText());
        Comment updatedComment = commentRepository.save(comment);
        return modelMapper.map(updatedComment, CommentDto.class);
    }


    @Override
    public void deleteCommentById(DeleteCommentRequest deleteCommentRequest) {
        Comment comment = commentRepository.findById(deleteCommentRequest.commentId())
                .orElseThrow(()-> new ResourceNotFound("Comment", "commentId", deleteCommentRequest.commentId().toString()));
        User user = userRepository.findById(deleteCommentRequest.userId())
                .orElseThrow(() -> new ResourceNotFound("User", "userId", deleteCommentRequest.userId().toString()));

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You are not authorized to delete this comment.");
        }
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFound("User", "userId", userId.toString()));
        commentRepository.deleteByUser(user);
    }

    @Override
    public void deleteCommentByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFound("Post", "postId", postId.toString()));
        commentRepository.deleteByPost(post);
    }

    @Override
    public CommentPageResponse getAllCommentsByPost(Long postId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFound("Post", "postId", postId.toString()));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> page_comment = commentRepository.findByPost(post, pageable);
        List<CommentDto> all_comments = page_comment.getContent().stream().map(comment -> this.modelMapper.map(comment, CommentDto.class)).toList();
        return new CommentPageResponse(all_comments,
                page_comment.getNumber(),
                page_comment.getSize(),
                page_comment.getTotalElements(),
                page_comment.getTotalPages(), page_comment.isLast());
    }

    @Override
    public CommentPageResponse getAllCommentsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFound("User", "userId", userId.toString()));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> page_comment = commentRepository.findByUser(user, pageable);
        List<CommentDto> all_comments = page_comment.getContent().stream().map(comment -> this.modelMapper.map(comment, CommentDto.class)).toList();
        return new CommentPageResponse(all_comments,
                page_comment.getNumber(),
                page_comment.getSize(),
                page_comment.getTotalElements(),
                page_comment.getTotalPages(), page_comment.isLast());

    }
}
