package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String email;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String hotelname;
    private String relation;

    public static BoardResponseDto entityToDto(Board board, User user){
        BoardResponseDto dto = BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .username(user.getUserid())
                .email(user.getEmail())
                .createdTime(board.getCreatedTime())
                .updatedTime(board.getUpdatedTime())
                .hotelname(board.getHotelname())
                .build();
        return dto;
    }

    public static BoardResponseDto entityToDto(AdminBoard board, User user){
        BoardResponseDto dto = BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .username(user.getUserid())
                .email(user.getEmail())
                .createdTime(board.getCreatedTime())
                .updatedTime(board.getUpdatedTime())
                .hotelname(board.getHotelname())
                .relation(board.getRelation())
                .build();
        return dto;
    }
}