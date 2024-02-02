package com.whl.hotelService.domain.common.dto;


import com.whl.hotelService.domain.common.entity.Hotel;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
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

    public static HotelDto entityToDto(Hotel hotel){
        HotelDto dto = HotelDto.builder()
                .hotelName(hotel.getHotelName())
                .region(hotel.getRegion())
                .addr1(hotel.getAddr1())
                .addr2(hotel.getAddr2())
                .contactInfo(hotel.getContactInfo())
                .build();
        return dto;
    }
}



