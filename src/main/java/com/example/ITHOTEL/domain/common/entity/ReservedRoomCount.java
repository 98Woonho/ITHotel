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
@Table(name="ReservedRoomCount")
public class ReservedRoomCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="room_id", foreignKey = @ForeignKey(name="fk_reservedRoomCount_room_id", foreignKeyDefinition = "FOREIGN KEY(room_id) REFERENCES room(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Room room;
    private String date;
    private int reservedCount = 1;
}
