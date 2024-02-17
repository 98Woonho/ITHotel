package com.whl.hotelService.domain.common.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardFileDto {
    private Long id;
    private String title;
    private String content;
    private MultipartFile file;
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장 파일 이름
    private Integer fileAttached; // 파일 첨부 여부 확인(1 첨부 0 미첨부)
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
