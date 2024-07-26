package org.prashant.blog.blogapplicationapi.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.auth.service.JwtService;
import org.prashant.blog.blogapplicationapi.auth.service.RefreshTokenService;
import org.prashant.blog.blogapplicationapi.auth.utils.AuthResponse;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.repository.RoleRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.prashant.blog.blogapplicationapi.utils.PasswordGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String profile_img = user.getAttribute("picture").toString();

        User dbuser = userRepository.findByEmail(email).orElseGet(() -> createUser(name, email, profile_img));
        var accessToken = jwtService.generateToken(dbuser);
        var refreshToken = refreshTokenService.createRefreshToken(email);

        // Create the AuthResponse with the user details and token
        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken.getRefreshToken(), dbuser);

        // Send AuthResponse as JSON in response body
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
    }
    private User createUser(String name, String email, String profile_img) {
        Role role = roleRepository.findById(AppConstant.NORMAL_USER)
                .orElseThrow(() -> new ResourceNotFound("Role", "role_id", AppConstant.NORMAL_USER.toString()));
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(PasswordGenerator.generatePassword()));
        user.setProfileImg(profile_img);
        user.getRoles().add(role);
        user.setAuthProvider(AppConstant.GOOGLE_AUTH_PROVIDER);
        return userRepository.save(user);
    }
}


