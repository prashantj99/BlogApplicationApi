package org.prashant.blog.blogapplicationapi.serviceimpl;

import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.entities.UserResponse;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.UserDto;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private  UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        User savedUser=this.userRepository.save(user);
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
        this.userRepository.delete(user);
    }

    @Override
    public UserResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        //set sorting criteria
        Sort sort = Sort.by(sortBy).ascending();
        if(sortDir.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }

        //create pageabel object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        //get requested users
        Page<User> page_user=this.userRepository.findAll(pageable);
        List<UserDto> users = page_user.getContent().stream().map( user -> this.modelMapper.map(user, UserDto.class)).toList();

        //create user response object
        UserResponse userResponse = new UserResponse();
        userResponse.setContent(users);
        userResponse.setLastPage(page_user.isLast());
        userResponse.setTotalPages(page_user.getTotalPages());
        userResponse.setRecords((int)page_user.getTotalElements());
        userResponse.setPageNumber(page_user.getNumber());
        userResponse.setPageSize(page_user.getSize());

        //return user response
        return userResponse;
    }
}
