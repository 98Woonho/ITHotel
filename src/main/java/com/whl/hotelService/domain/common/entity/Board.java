package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.common.dto.BoardDto;
import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "board")
public class Board extends BaseEntity {
    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    @Column(length = 20, nullable = false)
    private String title; //제목
    @Column(length = 500, nullable = false)
    private String content; // 내용
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
