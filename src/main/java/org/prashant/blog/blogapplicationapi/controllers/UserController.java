package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    final private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserByIdHandler(@PathVariable Long userId){
        UserDTO user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserHandler(@RequestBody UpdateUserRequest request){
        var user = this.userService.updateUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToCategory(@RequestBody SubscribeRequest request) {
        userService.subscribeToCategory(request.userId(), request.categoryId());
        return ResponseEntity.ok("");
    }
}


















