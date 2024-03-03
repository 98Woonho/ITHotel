package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 인덱스 번호

    @Column(nullable = false)
    private String content; // 관리자 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_board_id", foreignKey = @ForeignKey(name = "fk_comment_admin_board_id", foreignKeyDefinition = "FOREIGN KEY(admin_board_id) REFERENCES admin_board(id) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private AdminBoard adminBoard; // 관리자게시판을 부모로 댓글을 외래키로 걸어 관리자게시판 삭제시 댓글도 함께 삭제하기 위한 join

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="fk_comment_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user; // 관리자가 댓글 작성했을 시, 본인이 쓴 댓글을 수정 및 삭제하기 위한 entity

    public void update(String content) {
        this.content = content;
    }
}
