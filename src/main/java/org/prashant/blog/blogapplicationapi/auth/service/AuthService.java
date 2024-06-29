package org.prashant.blog.blogapplicationapi.auth.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.auth.utils.AuthResponse;
import org.prashant.blog.blogapplicationapi.auth.utils.LoginRequest;
import org.prashant.blog.blogapplicationapi.auth.utils.RegisterRequest;
import org.prashant.blog.blogapplicationapi.entities.RefreshToken;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.InvalidParameterException;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.UserDto;
import org.prashant.blog.blogapplicationapi.repository.RoleRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public AuthResponse register(RegisterRequest registerRequest){
        if(!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())){
            throw new InvalidParameterException("repeat password mismatch");
        }
        Role role = this.roleRepository.findById(AppConstant.NORMAL_USER).orElseThrow(()->new ResourceNotFound("Role", "role_id", "501"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        var user = User.builder()
                .name(registerRequest.getUsername())
                .userEmail(registerRequest.getEmail())
                .userPassword(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(roles)
                .build();
        User savedUser = userRepository.save(user);
        System.out.println(savedUser);//debug
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getUserEmail());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .userDto(modelMapper.map(user, UserDto.class))
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    //login
    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        User user = userRepository.findByUserEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFound("User", "userEmail", loginRequest.getEmail()));
        var accessToken = this.jwtService.generateToken(user);
        var refreshToken = this.refreshTokenService.createRefreshToken(user.getUserEmail());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .userDto(modelMapper.map(user, UserDto.class))
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
}
