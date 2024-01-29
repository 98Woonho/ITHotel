package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.Hotel;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HotelDto {
    private String hotelname; // 호텔 이름
    private String region;
    private String address; // 호텔 주소
    private String contactInfo; // 호텔 연락처

    public static HotelDto entityToDto(Hotel hotel){
        HotelDto dto = HotelDto.builder()
                .hotelname(hotel.getHotelname())
                .region(hotel.getRegion())
                .address(hotel.getAddress())
                .contactInfo(hotel.getContactInfo())
                .build();
        return dto;
    }
}