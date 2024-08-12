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
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.CommentService;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private  final UserService userService;
    
    @Override
    public CommentDTO createComment(CreateCommentRequest createCommentRequest) {
        Post post = postRepository.findById(createCommentRequest.postId())
                .orElseThrow(()-> new ResourceNotFound("Post", "postId", createCommentRequest.postId().toString()));

        User user = userService.getLoggedInUser()
                .orElseThrow(()-> new UnAuthorizedOperationExcpetion("Your are not authorized"));

        Comment comment = new Comment();
        comment.setCommentText(createCommentRequest.commentText());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCommentDate(new Date());
        comment.setLastEdited(new Date());

        Comment saved_comment = commentRepository.save(comment);
        return new CommentDTO(saved_comment);
    }

    @Override
    @PreAuthorize("#comment.getUser().getUsername() == authentication.name")
    public CommentDTO updateComment(UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new ResourceNotFound("Comment", "commentId", request.commentId().toString()));
        comment.setCommentText(request.commentText());
        Comment updatedComment = commentRepository.save(comment);
        return new CommentDTO(updatedComment);
    }

    @Override
    @PreAuthorize("#comment.getUser().getUsername() == authentication.name")
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFound("Comment", "commentId", commentId.toString()));
        commentRepository.delete(comment);
    }

    @Override
    public CommentPageResponse getAllCommentsByPost(Long postId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFound("Post", "postId", postId.toString()));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> page_comment = commentRepository.findByPost(post, pageable);
        List<CommentDTO> all_comments = page_comment.getContent().stream().map(CommentDTO::new).toList();
        return new CommentPageResponse(all_comments,
                page_comment.getNumber(),
                page_comment.getSize(),
                page_comment.getTotalElements(),
                page_comment.getTotalPages(), page_comment.isLast());
    }

    @Override
    public CommentPageResponse getAllCommentsByUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = userService.getLoggedInUser()
                .orElseThrow(()-> new UnAuthorizedOperationExcpetion("Unauthorized Access to Resource!!!"));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> page_comment = commentRepository.findByUser(user, pageable);
        List<CommentDTO> all_comments = page_comment.getContent().stream().map(CommentDTO::new).toList();
        return new CommentPageResponse(all_comments,
                page_comment.getNumber(),
                page_comment.getSize(),
                page_comment.getTotalElements(),
                page_comment.getTotalPages(), page_comment.isLast());

    }
}
