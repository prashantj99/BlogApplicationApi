package org.prashant.blog.blogapplicationapi.auth.service;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.auth.utils.AuthResponse;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.repository.RoleRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.prashant.blog.blogapplicationapi.utils.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email).orElseGet(() -> createUser(name, email));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(email);

        // Create the AuthResponse with the user details and token
        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken.getRefreshToken(), user);

        // Add the AuthResponse to the attributes
        attributes.put("authResponse", authResponse);

        return oAuth2User;
    }

    private User createUser(String name, String email) {
        Role role = roleRepository.findById(AppConstant.NORMAL_USER)
                .orElseThrow(() -> new ResourceNotFound("Role", "role_id", AppConstant.NORMAL_USER.toString()));
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(PasswordGenerator.generatePassword()));
        user.setProfileImg(AppConstant.DEFAULT_USER_PROFILE_PIC_NAME);
        user.getRoles().add(role);
        user.setAuthProvider(AppConstant.GOOGLE_AUTH_PROVIDER);
        return userRepository.save(user);
    }
}
