package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notice_board")
public class NoticeBoard extends BaseEntity {
    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    @Column(length = 250, nullable = false)
    private String title; //제목
    @Column(length = 500, nullable = false)
    private String content; // 내용
    private int fileAttached;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_notice_board_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user;
}
