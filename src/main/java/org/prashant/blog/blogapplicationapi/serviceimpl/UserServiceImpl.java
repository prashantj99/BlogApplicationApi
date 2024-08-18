package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.*;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.repository.CategoryRepository;
import org.prashant.blog.blogapplicationapi.repository.PostRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.CategoryService;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public UserDTO getUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFound("User", "userId", userId.toString()));
        return new UserDTO(user);
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest request) {
        var user = this.userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFound("User", "user_id", request.userId().toString()));

        // Update user details
        user.setAbout(request.about());
        user.setName(request.name());

        // Clear the existing accounts and add the new ones
        user.getAccounts().clear();
        List<Account> newAccounts = request.accounts().stream().map(account -> {
            Account acc = new Account();
            acc.setAccountId(account.accountId());
            acc.setUser(user);
            acc.setLink(account.link());
            acc.setPlatform(account.platform());
            return acc;
        }).toList();

        user.getAccounts().addAll(newAccounts);

        // Save updated user
        var savedUser = userRepository.save(user);

        // Return updated user details
        return new UserDTO(savedUser);
    }

    @Override
    public void subscribeToCategory(Long userId, Long categoryId) {
        System.out.println("userId"+userId);
        System.out.println("catId"+categoryId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.toString()));
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category", "categoryId", categoryId.toString()));

        if (user.getSubscribedCategories().contains(category)) {
            user.getSubscribedCategories().remove(category);
        } else {
            user.getSubscribedCategories().add(category);
        }
        userRepository.save(user);
    }

    @Override
    public PostPageResponse getPostsFromSubscribedCategories(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        var user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFound("User", "userId", userId.toString()));
        //get user subscribed categories
        List<Category> subscribedCategories = user.getSubscribedCategories();
        subscribedCategories.forEach(category -> System.out.println(category.getTitle()));
        //make page request
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> postsPage = postRepository.findByCategoryIn(subscribedCategories, pageable);
        List<PostDTO> posts = postsPage.getContent().stream().map(PostDTO::new).toList();
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
    public Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            System.out.println(user);
            return Optional.of(user);
        }
        return null;
    }
}
