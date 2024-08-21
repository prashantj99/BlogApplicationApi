package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.Comment;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.exceptions.UnAuthorizedOperationExcpetion;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.repository.CommentRepository;
import org.prashant.blog.blogapplicationapi.repository.PostRepository;
import org.prashant.blog.blogapplicationapi.service.CommentService;
import org.prashant.blog.blogapplicationapi.service.UserService;
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
    private final UserService userService;

    @Override
    public CommentDTO createComment(CreateCommentRequest createCommentRequest) {
        Post post = postRepository.findById(createCommentRequest.postId())
                .orElseThrow(() -> new ResourceNotFound("Post", "postId", createCommentRequest.postId().toString()));

        User user = userService.getLoggedInUser()
                .orElseThrow(() -> new UnAuthorizedOperationExcpetion("You are not authorized"));

        Comment comment = new Comment();
        comment.setCommentText(createCommentRequest.commentText());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCommentDate(new Date());
        comment.setLastEdited(new Date());

        Comment savedComment = commentRepository.save(comment);
        return new CommentDTO(savedComment);
    }

    @Override
    public CommentDTO updateComment(UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new ResourceNotFound("Comment", "commentId", request.commentId().toString()));

        User currentUser = userService.getLoggedInUser()
                .orElseThrow(() -> new UnAuthorizedOperationExcpetion("You are not authorized"));

        if (!comment.getUser().getUsername().equals(currentUser.getUsername())) {
            throw new UnAuthorizedOperationExcpetion("You are not authorized to update this comment");
        }

        comment.setCommentText(request.commentText());
        comment.setLastEdited(new Date());

        Comment updatedComment = commentRepository.save(comment);
        return new CommentDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("Comment", "commentId", commentId.toString()));

        User currentUser = userService.getLoggedInUser()
                .orElseThrow(() -> new UnAuthorizedOperationExcpetion("You are not authorized"));

        if (!comment.getUser().getUsername().equals(currentUser.getUsername())) {
            throw new UnAuthorizedOperationExcpetion("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public CommentPageResponse getAllCommentsByPost(Long postId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFound("Post", "postId", postId.toString()));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> pageComment = commentRepository.findByPost(post, pageable);

        List<CommentDTO> allComments = pageComment.getContent().stream().map(CommentDTO::new).toList();
        return new CommentPageResponse(allComments,
                pageComment.getNumber(),
                pageComment.getSize(),
                pageComment.getTotalElements(),
                pageComment.getTotalPages(),
                pageComment.isLast());
    }

    @Override
    public CommentPageResponse getAllCommentsByUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = userService.getLoggedInUser()
                .orElseThrow(() -> new UnAuthorizedOperationExcpetion("Unauthorized Access to Resource!!!"));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> pageComment = commentRepository.findByUser(user, pageable);

        List<CommentDTO> allComments = pageComment.getContent().stream().map(CommentDTO::new).toList();
        return new CommentPageResponse(allComments,
                pageComment.getNumber(),
                pageComment.getSize(),
                pageComment.getTotalElements(),
                pageComment.getTotalPages(),
                pageComment.isLast());
    }
}
