package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardFileInfo extends BaseEntity{

    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne
    @JoinColumn(name="board", foreignKey = @ForeignKey(name="fk_board_file_info_board", foreignKeyDefinition = "FOREIGN KEY(board) REFERENCES board(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Board board;

    private String dir;
    private String fileName;
}
