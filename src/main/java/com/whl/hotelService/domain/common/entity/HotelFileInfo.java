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
@Entity // 이 클래스가 DB에 있는 테이블을 의미함.
public class HotelFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // = Auto increment
    private Long id;

    @ManyToOne
    @JoinColumn(name="hotelName", foreignKey = @ForeignKey(name="fk_hotelFileInfo_hotelName", foreignKeyDefinition = "FOREIGN KEY(hotelName) REFERENCES hotel(hotel_name) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private Hotel hotel;
    private String dir;
    private String fileName;
}