package com.example.ITHOTEL.domain.common.entity;

import com.example.ITHOTEL.domain.user.entity.User;
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
    private Long id; // 인덱스 번호
    @Column(length = 250, nullable = false)
    private String title; // 공지글 제목
    @Column(length = 500, nullable = false)
    private String content; // 공지글 내용
    private int fileAttached; // 공지글 파일 첨부 여부
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_notice_board_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user; // 관리자가 쓴 공지게시판을 수정 및 삭제하기 위한 entity
}
