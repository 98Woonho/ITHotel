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
    private String hotelName; // 호텔 이름
    private String region;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String contactInfo; // 호텔 연락처
    private String hotelDetails;
}
