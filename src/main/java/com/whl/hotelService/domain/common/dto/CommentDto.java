package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.Comment;
import com.whl.hotelService.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Long id;
    private String content;
    private String username;
    private String email;
    private AdminBoard board;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static CommentDto entityToDto(Comment comment, AdminBoard board, User user) {
        CommentDto dto = CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(user.getName())
                .email(user.getEmail())
                .board(comment.getAdminBoard())
                .createdTime(comment.getCreatedTime())
                .updatedTime(comment.getUpdatedTime())
                .build();
        return dto;
    }
}