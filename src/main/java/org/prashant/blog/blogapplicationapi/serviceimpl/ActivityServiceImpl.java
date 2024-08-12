package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.Activity;
import org.prashant.blog.blogapplicationapi.entities.ActivityType;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.ActivityDTO;
import org.prashant.blog.blogapplicationapi.payload.PostDTO;
import org.prashant.blog.blogapplicationapi.payload.PostPageResponse;
import org.prashant.blog.blogapplicationapi.repository.ActivityRepository;
import org.prashant.blog.blogapplicationapi.repository.PostRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    @Override
    public ActivityDTO performActivityOnPost(Long userId, Long postId, ActivityType activityType) {
        var user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.toString()));
        var post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFound("Post", "postId", postId.toString()));

        Activity activity = this.activityRepository.findByUserAndPostAndActivityType(user, post, activityType)
                .map(act -> {
                    this.activityRepository.delete(act);
                    act.setActivityType(null);
                    return act;
                })
                .orElseGet(() -> {
                    Activity newActivity = new Activity();
                    newActivity.setActivityDate(LocalDateTime.now());
                    newActivity.setActivityType(activityType);
                    newActivity.setUser(user);
                    newActivity.setPost(post);
                    return this.activityRepository.save(newActivity);
                });
        return new ActivityDTO(activity);
    }

    @Override
    public PostPageResponse getPostsByActivityType(Long userId, ActivityType activityType, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        //get user form db
        var user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.toString()));

        //get required activities
        Page<Activity> page_activities = activityRepository.findByUserAndActivityType(user, activityType, pageable);

        //get posts form activity
        List<PostDTO> posts = page_activities.getContent().stream().map(activity -> new PostDTO(activity.getPost())).toList();

        //return payload
        return new PostPageResponse(posts,
                page_activities.getNumber(),
                page_activities.getSize(),
                page_activities.getTotalElements(),
                page_activities.getTotalPages(), page_activities.isLast());
    }


}
