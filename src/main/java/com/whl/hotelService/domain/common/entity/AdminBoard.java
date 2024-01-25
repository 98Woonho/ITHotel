package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "admin_board")
public class AdminBoard extends BaseEntity {
    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    @Column(length = 20, nullable = false)
    private String title; //제목
    @Column(length = 500, nullable = false)
    private String content; // 내용

    private String boardType; //게시판 종류 공지, 어드민
    @ManyToOne
    @JoinColumn(name = "user_userid", foreignKey = @ForeignKey(name="fk_adminBoard_user_userid", foreignKeyDefinition = "FOREIGN KEY(user_userid) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user;

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}