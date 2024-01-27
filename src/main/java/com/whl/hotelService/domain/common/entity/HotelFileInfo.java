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
    @JoinColumn(name="hotelname", foreignKey = @ForeignKey(name="fk_hotelFileInfo_hotelname", foreignKeyDefinition = "FOREIGN KEY(hotelname) REFERENCES hotel(hotelname) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private Hotel hotel;
    private String dir;
    private String filename;
}
