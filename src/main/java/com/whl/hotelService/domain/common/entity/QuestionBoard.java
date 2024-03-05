package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "question_board")
public class QuestionBoard extends BaseEntity {
    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id; // 인덱스 번호
    @Column(length = 250, nullable = false)
    private String title; // 자주하는 질문 제목
    @Column(length = 500, nullable = false)
    private String content; // 자주하는 질문 내용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_question_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user; // 관리자가 자주하는 질문을 작성했을 시, 본인이 작성한 글을 수정 및 삭제 하기 위한 entity

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
