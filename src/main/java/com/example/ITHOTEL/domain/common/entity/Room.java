package com.example.ITHOTEL.domain.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="hotel_name", foreignKey = @ForeignKey(name="fk_room_hotel_name", foreignKeyDefinition = "FOREIGN KEY(hotel_name) REFERENCES hotel(hotel_name) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Hotel hotel;

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
}
