package com.myagent.backend.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record EventCreateRequest(
        @NotBlank(message = "이벤트 이름은 비워둘 수 없습니다")
        @Size(max = 100, message = "100자 이하만 등록이 가능합니다")
        String title,
        @NotNull(message = "날짜를 입력해주세요")
        Instant startsAt,
        Instant endsAt,
        boolean allDay,
        @Size(max = 100, message = "100자 이하만 등록이 가능합니다")
        String place,
        @Size(max = 200, message = "200자 이하만 등록이 가능합니다")
        String memo
) {
}
