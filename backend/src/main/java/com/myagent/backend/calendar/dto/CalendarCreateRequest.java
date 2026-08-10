package com.myagent.backend.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CalendarCreateRequest(
        @NotBlank(message = "캘린더 이름을 입력하세요")
        @Size(max = 30, message = "이름은 30자 이하여야 합니다")
        String name
) { }
