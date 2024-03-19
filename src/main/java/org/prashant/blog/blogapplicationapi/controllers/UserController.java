package org.prashant.blog.blogapplicationapi.controllers;

import org.prashant.blog.blogapplicationapi.entities.ApiResponse;
import org.prashant.blog.blogapplicationapi.entities.UserResponse;
import org.prashant.blog.blogapplicationapi.payload.UserDto;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<UserResponse> getUsers(
                                                 @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
                                                 @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                 @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_USER_SORT_FIELD, required = false) String sortBy,
                                                 @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir

    ){
        UserResponse userResponse = this.userService.getUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto created_user = this.userService.createUser(userDto);
        return new ResponseEntity<>(created_user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        this.userService.deleteUser(userId);
        return  new ResponseEntity<>(new ApiResponse("user deleted successfully", true), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto){
        UserDto updatedUser = this.userService.updateUser(userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}


















