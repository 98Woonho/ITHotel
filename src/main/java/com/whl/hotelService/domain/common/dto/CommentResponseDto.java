package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Long id;
    private String content;
    private String username;
    private String email;

    @Builder //Entity를 Dto로 바꾸는거
    public CommentResponseDto(Comment comment) {
        this.createdTime = comment.getCreatedTime();
        this.updatedTime = comment.getUpdatedTime();
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getName();
        this.email = comment.getUser().getEmail();
    }
}