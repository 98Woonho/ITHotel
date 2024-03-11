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
@Entity // 이 클래스가 DB에 있는 테이블을 의미함.
public class RoomFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // = Auto increment
    private Long id;

    @ManyToOne
    @JoinColumn(name="room_id", foreignKey = @ForeignKey(name="fk_roomFileInfo_room_id", foreignKeyDefinition = "FOREIGN KEY(room_id) REFERENCES room(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Room room; // 객실 정보

    private String dir;
    private String fileName;

    @Column(name="main_image_flag")
    private boolean isMainImage;
}
