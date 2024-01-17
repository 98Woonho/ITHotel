package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private String username;
    private String email;

    //검색 타입
    private String type;



    @Builder  //Entity를 Dto로 변환해서 리턴
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdTime = board.getCreatedTime();
        this.updatedTime = board.getUpdatedTime();
        this.username = board.getUser().getId();
        this.email = board.getUser().getEmail();
        this.type = board.getType();
    }
}
