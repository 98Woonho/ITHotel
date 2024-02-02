package com.whl.hotelService.domain.common.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HotelDto {
    private String hotelName; // 호텔 이름
    private String region;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String contactInfo; // 호텔 연락처
    private String hotelDetails;
    private MultipartFile[] files;
    private String[] fileNames;
    private MultipartFile[] mainFiles;
    private String mainFileName;
    private String[] existingFileNames;
}
