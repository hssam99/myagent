package com.myagent.backend.user.repository;

import com.myagent.backend.user.entity.OAuthProvider;
import com.myagent.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId);
}
