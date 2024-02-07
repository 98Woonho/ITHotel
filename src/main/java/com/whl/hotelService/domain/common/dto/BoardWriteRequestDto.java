package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.NoticeBoard;
import com.whl.hotelService.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardWriteRequestDto {

    private Long id;
    private String title;
    private String content;
    private String hotelname;
    private String relation;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private MultipartFile file;
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장 파일 이름
    private int fileAttached; // 파일 첨부 여부 확인(1 첨부 0 미첨부)

    public static BoardWriteRequestDto entityToDto(NoticeBoard board){
        BoardWriteRequestDto dto = BoardWriteRequestDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdTime(board.getCreatedTime())
                .updatedTime(board.getUpdatedTime())
                .fileAttached(board.getFileAttached())
                .originalFileName(board.getOriginalFileName())
                .storedFileName(board.getStoredFileName())
                .build();
        return dto;
    }
}
