package com.whl.hotelService.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="hotel")
public class Hotel {
    @Id
    private String hotelname; // 호텔 이름

    private String region;
    @Column(nullable = false)
    private String address; // 호텔 주소
    @Column(nullable = false)
    private String contactInfo; // 호텔 연락처
}
