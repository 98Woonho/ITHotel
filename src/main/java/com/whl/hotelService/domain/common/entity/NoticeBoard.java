package com.whl.hotelService.domain.common.entity;

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
@Table(name = "notice_board")
public class NoticeBoard extends BaseEntity {
    @Id //PrimaryKey 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    @Column(length = 250, nullable = false)
    private String title; //제목
    @Column(length = 500, nullable = false)
    private String content; // 내용
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장 파일 이름
    private int fileAttached;
}
