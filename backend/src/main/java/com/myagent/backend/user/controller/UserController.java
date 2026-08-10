package com.myagent.backend.user.controller;

import com.myagent.backend.user.dto.UserMeResponse;
import com.myagent.backend.user.entity.OAuthProvider;
import com.myagent.backend.user.entity.User;
import com.myagent.backend.user.repository.UserRepository;
import com.myagent.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserMeResponse me(OAuth2AuthenticationToken authentication) {

        User user = userService.getCurrentUser(authentication);

        return UserMeResponse.from(user);
    }
}
