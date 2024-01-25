package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="reservation_id", foreignKey = @ForeignKey(name="fk_payment_reservation_id", foreignKeyDefinition = "FOREIGN KEY(reservation_id) REFERENCES reservation(id) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private Reservation reservation; // 예약 정보

    @ManyToOne
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name="fk_payment_user_userid", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User userid; // 사용자 정보

    // 결제 정보
    @Column(nullable = false)
    private String impUid;
    @Column(nullable = false)
    private String merchantUid;
    @Column(nullable = false)
    private String payMethod;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long paidAmount;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String address;

    // 결제 날짜
    @Column(nullable = false ,columnDefinition = "DATETIME(6)")
    private LocalDateTime payDate;
}
