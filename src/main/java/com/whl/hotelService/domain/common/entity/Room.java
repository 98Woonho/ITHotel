package com.whl.hotelService.domain.common.entity;


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
    @JoinColumn(name="hotel_name", foreignKey = @ForeignKey(name="fk_room_hotel_name", foreignKeyDefinition = "FOREIGN KEY(hotel_name) REFERENCES hotel(name) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private Hotel hotelName;

    @Column(nullable = false)
    private String kind; // 객실 종류
    @Column(nullable = false)
    private String checkinTime; // 입실 시간
    @Column(nullable = false)
    private String checkoutTime; // 퇴실 시간
    @Column(nullable = false)
    private int standardPeople; // 기준 인원
    @Column(nullable = false)
    private int maximumPeople; // 최대 인원
    @Column(nullable = false)
    private int weekendPrice; // 주말 가격
    @Column(nullable = false)
    private int weekdayPrice; // 주중 가격
    @Column(nullable = false)
    private int count; // 객실의 개수
}
