package com.whl.hotelService.domain.common.dto;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class ReservationDto {
    private Long id;
    private Long roomId; // 객실 정보
    private String userid; // 사용자 정보
    private String checkin; // 체크인
    private String checkout; // 체크아웃
    private String status; // 예약 상태
    private String people; // 인원
    private Date createdAt;
}
