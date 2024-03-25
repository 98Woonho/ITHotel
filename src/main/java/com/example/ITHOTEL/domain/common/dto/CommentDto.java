package com.example.ITHOTEL.domain.common.dto;

import com.example.ITHOTEL.domain.common.entity.InquiryBoard;
import com.example.ITHOTEL.domain.common.entity.Comment;
import com.example.ITHOTEL.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Long id;
    private String content;
    private String username;
    private String email;
    private InquiryBoard board;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static CommentDto entityToDto(Comment comment, InquiryBoard board, User user) {
        CommentDto dto = CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(user.getName())
                .email(user.getEmail())
                .board(comment.getInquiryboard())
                .createdTime(comment.getCreatedTime())
                .updatedTime(comment.getUpdatedTime())
                .build();
        return dto;
    }
}