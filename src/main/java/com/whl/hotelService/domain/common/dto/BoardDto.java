package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.NoticeBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
