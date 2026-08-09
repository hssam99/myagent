package com.myagent.backend.user.dto;

import com.myagent.backend.user.entity.User;

public record UserMeResponse (
        Long id,
        String email,
        String nickname,
        String timezone
){
    public static UserMeResponse from(User user){
        return new UserMeResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getTimezone()
        );
    }
}
