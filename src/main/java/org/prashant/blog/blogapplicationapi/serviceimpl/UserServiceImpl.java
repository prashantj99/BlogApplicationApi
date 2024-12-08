package org.prashant.blog.blogapplicationapi.serviceimpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.Account;
import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.PostDTO;
import org.prashant.blog.blogapplicationapi.payload.PostPageResponse;
import org.prashant.blog.blogapplicationapi.payload.UpdateUserRequest;
import org.prashant.blog.blogapplicationapi.payload.UserDTO;
import org.prashant.blog.blogapplicationapi.repository.CategoryRepository;
import org.prashant.blog.blogapplicationapi.repository.PostRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
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
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.toString()));
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category", "categoryId", categoryId.toString()));

        if (user.getSubscribedCategories().contains(category)) {
            user.getSubscribedCategories().remove(category);
            category.getSubscribers().remove(user);
        } else {
            user.getSubscribedCategories().add(category);
            category.getSubscribers().add(user);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            var dbuser = userRepository.findById(user.getUserId()).orElseThrow(()-> new RuntimeException("Internal Error Server"));
            return Optional.of(dbuser);
        }
        return null;
    }
}
