package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.payload.UserPageResponse;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.UserDto;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        System.out.println("debug user impl");
        user.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        User savedUser = this.userRepository.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = this.userRepository.findById(userDto.getUserId())
                .orElseThrow(() -> new ResourceNotFound("User", "user_id", String.valueOf(userDto.getUserId())));
        user.setUserEmail(userDto.getUserEmail());
        user.setImgUrl(userDto.getImgUrl());
        user.setAbout(userDto.getAbout());
        User updatedUser = this.userRepository.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user=this.userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFound("User", "userId", String.valueOf(userId)));
        System.out.println("debug"+user);
        for(Role role : user.getRoles()){
            if(Objects.equals(role.getId(), AppConstant.ADMIN_USER)){
                throw new RuntimeException("ADMIN Cannot be deleted");
            }
        }
        this.userRepository.delete(user);
    }

    @Override
    public UserPageResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        //set sorting criteria
        Sort sort = Sort.by(sortBy).ascending();
        if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        }

        //create pageabel object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        //get requested users
        Page<User> page_user = this.userRepository.findAll(pageable);
        List<UserDto> users = page_user.getContent().stream().map(user -> this.modelMapper.map(user, UserDto.class)).toList();

        //return user response
        return new UserPageResponse(users,
                page_user.getNumber(), page_user.getSize(),
                page_user.getTotalElements(),
                page_user.getTotalPages(), page_user.isLast());
    }
}
