package com.whl.hotelService.domain.common.entity;

import io.jsonwebtoken.io.IOException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Entity
public class NoticeBoardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이미지 인덱스 확인용
    private String name; // 공지게시판 이미지 이름
    private String type; // 공지게시판 이미지 타입
    private int size; // 공지게시판 이미지 크기
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data; // 공지게시판 이미지 데이터 저장 용량
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="notice_board_id", foreignKey = @ForeignKey(name="fk_notice_board_image_notice_board_id", foreignKeyDefinition = "FOREIGN KEY(notice_board_id) REFERENCES notice_board(id) ON DELETE CASCADE ON UPDATE CASCADE"))
    private NoticeBoard noticeBoard; // 공지게시판 부모키로 이미지를 외래키로 조인하여 공지게시판 삭제시 이미지파일도 같이 삭제
    public NoticeBoardImage(MultipartFile file) throws IOException, java.io.IOException {
        super();
        this.name = file.getOriginalFilename();
        this.type = file.getContentType();
        this.size = (int) file.getSize();
        this.data = file.getBytes();
    }
}
