package com.myagent.backend.user.service;

import com.myagent.backend.user.entity.OAuthProvider;
import com.myagent.backend.user.entity.User;
import com.myagent.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // 구글이 준 정보 받기 (sub, email, name)

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // → 팬이 어느 버튼(주소)으로 시작했는지에 따라 "google" 또는 "kakao" application.properties
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());
        // → 그 문자열을 대문자로 바꿔서("GOOGLE") 우리 enum으로 변환
        // OAuthProvider.valueOf("GOOGLE") "이 문자열과 이름이 똑같은 enum 상수를 찾아서 돌려줘"

        // TODO: 제공자 추가 시 속성 키 분기 필요 (google=sub, kakao=id)
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .nickname(name)
                                .provider(provider)
                                .providerId(providerId)
                                .build()
                ));

        return oAuth2User;
    }
}
