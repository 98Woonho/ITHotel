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
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_board_id", foreignKey = @ForeignKey(name = "fk_comment_admin_board_id", foreignKeyDefinition = "FOREIGN KEY(admin_board_id) REFERENCES admin_board(id) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private AdminBoard adminBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="fk_comment_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(userid) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user;

    public void update(String content) {
        this.content = content;
    }
}
