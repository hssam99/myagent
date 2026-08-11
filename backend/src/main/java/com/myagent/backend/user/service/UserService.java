package com.myagent.backend.user.service;

import com.myagent.backend.common.exception.BusinessException;
import com.myagent.backend.common.exception.ErrorCode;
import com.myagent.backend.user.entity.OAuthProvider;
import com.myagent.backend.user.entity.User;
import com.myagent.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser(OAuth2AuthenticationToken authentication) {
        OAuthProvider provider = OAuthProvider.valueOf(
                authentication.getAuthorizedClientRegistrationId().toUpperCase());
        return userRepository.findByProviderAndProviderId(provider, authentication.getName())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
