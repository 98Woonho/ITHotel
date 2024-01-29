package com.whl.hotelService.domain.common.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RoomDto {
    private Long id;
    private String hotelname;
    private String kind; // 객실 종류
    private String checkinTime; // 입실 시간
    private String checkoutTime; // 퇴실 시간
    private int standardPeople; // 기준 인원
    private int maximumPeople; // 최대 인원
    private int fridayPrice; // 금요일 숙박 가격
    private int saturdayPrice; // 토요일 숙박 가격
    private int weekdayPrice; // 주중 가격
    private int count; // 객실의 개수
    private Integer reservedRoomCount; // 남은 방 개수
    private MultipartFile[] files;
    private String[] fileNames;
    private MultipartFile[] mainFiles;
    private String mainFileName;
}
