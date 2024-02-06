package com.whl.hotelService.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardWriteRequestDto {

    private Long id;
    private String title;
    private String content;
    private String hotelname;
    private String relation;
    private MultipartFile[] files;
    private String[] fileNames;
    private MultipartFile[] mainFiles;
    private String mainFileName;
    private String[] existingFileNames;
}
