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
    @Column(name = "commentid")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_boardid", foreignKey = @ForeignKey(name = "fk_comment_board_boardid", foreignKeyDefinition = "FOREIGN KEY(board_boardid) REFERENCES board(boardid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_userid", foreignKey = @ForeignKey(name="fk_comment_user_userid", foreignKeyDefinition = "FOREIGN KEY(user_userid) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user;

    public void update(String content) {
        this.content = content;
    }
}
