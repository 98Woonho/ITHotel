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
@Table(name = "notice_board_file")
public class NoticeBoardFileInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 파일 인덱스
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장 파일 이름
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="notice_board_id", foreignKey = @ForeignKey(name="fk_notice_board_file_info_notice_board_id", foreignKeyDefinition = "FOREIGN KEY(notice_board_id) REFERENCES notice_board(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private NoticeBoard noticeBoard;  // 공지게시글을 부모키로 fileInfo를 외래키를 걸어 공지게시글이 삭제되었을 시 파일도 함께 삭제하기 위한 entity

}
