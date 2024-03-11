package com.example.ITHOTEL.domain.common.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String username;
    private String userid;
    private String email;
    private String hotelname;
    private String relation;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
