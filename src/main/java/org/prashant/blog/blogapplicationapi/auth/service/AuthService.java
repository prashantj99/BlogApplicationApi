package org.prashant.blog.blogapplicationapi.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.auth.utils.AuthResponse;
import org.prashant.blog.blogapplicationapi.auth.utils.LoginRequest;
import org.prashant.blog.blogapplicationapi.auth.utils.RegisterRequest;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.InvalidParameterException;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.repository.RoleRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    //register user service method
    public AuthResponse register(RegisterRequest request){
        if(!request.getPassword().equals(request.getRepeatPassword())){
            throw new InvalidParameterException("repeat password mismatch");
        }

        //assign default role to user
        Role role = this.roleRepository.findById(AppConstant.NORMAL_USER).orElseThrow(()->new ResourceNotFound("Role", "role_id", "501"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        //build user entity
        User user = new User();
        user.setName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setAuthProvider(AppConstant.DEFAULT_AUTH_PROVIDER);
        user.setProfileImg(AppConstant.DEFAULT_USER_PROFILE_PIC_NAME);

        //save user to database
        User savedUser = userRepository.save(user);

        //generate token accessToken and refresh token
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getUsername());

        //auth response
        return new AuthResponse(accessToken, refreshToken.getRefreshToken(), user);
    }

    //login service method
    public AuthResponse login(LoginRequest request, HttpServletResponse response){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        //get user from db
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFound("User", "Email", request.getEmail()));

        //generate tokens
        var accessToken = this.jwtService.generateToken(user);
        var refreshToken = this.refreshTokenService.createRefreshToken(user.getUsername());

        //debug
        user.getRoles().forEach((role -> System.out.println(role.getName())));

        //add refresh token to cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(AppConstant.cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        //auth response
        return new AuthResponse(accessToken, refreshToken.getRefreshToken(),user);
    }
}
