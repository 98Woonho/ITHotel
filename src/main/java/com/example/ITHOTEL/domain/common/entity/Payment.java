package com.example.ITHOTEL.domain.common.entity;

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
    @JoinColumn(name="reservation_id", foreignKey = @ForeignKey(name="fk_payment_reservation_id", foreignKeyDefinition = "FOREIGN KEY(reservation_id) REFERENCES reservation(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Reservation reservation; // 예약 정보

    // 결제 정보
    private String impUid;
    private String merchantUid;
    private String payMethod;
    private String name;
    private Long paidAmount;
    private String status;
    private String address;

    // 결제 날짜
    private LocalDateTime payDate;
}
