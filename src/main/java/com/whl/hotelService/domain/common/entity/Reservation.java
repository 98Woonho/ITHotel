package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="room_id", foreignKey = @ForeignKey(name="fk_reservation_room_id", foreignKeyDefinition = "FOREIGN KEY(room_id) REFERENCES room(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Room room; // 객실 정보

    @ManyToOne
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name="fk_reservation_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"))

    private User user; // 사용자 정보
    private String checkin; // 체크인
    private String checkout; // 체크아웃
    private String status; // 예약 상태
    private String people; // 인원
    private LocalDateTime createdAt; // 생성 시간
    private int price; // 결제 금액
}
