package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "admin_board")
public class              AdminBoard extends BaseEntity {
    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id; // 인덱스 번호
    @Column(length = 250, nullable = false)
    private String title; // 1:1 고객문의 문의제목
    @Column(length = 500, nullable = false)
    private String content; // 1:1 고객문의 문의내용
    private String hotelname; // 1:1 고객문의 호텔이름
    private String relation; // 1:1 고객문의 관련문의
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="fk_adminBoard_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user; // 어떤 회원이 문의글을 작성했는지 확인하기 위한 entity
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}