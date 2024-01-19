package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.common.entity.Comment;
import com.whl.hotelService.domain.user.entity.User;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private String username;
    private String email;
    private Board board;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static CommentResponseDto entityToDto(Comment comment, Board board, User user) {
        CommentResponseDto dto = CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(user.getName())
                .email(user.getEmail())
                .board(comment.getBoard())
                .createdTime(comment.getCreatedTime())
                .updatedTime(comment.getUpdatedTime())
                .build();
        return dto;
    }
}