package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.CommentDto;
import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.Comment;
import com.whl.hotelService.domain.common.repository.AdminBoardRepository;
import com.whl.hotelService.domain.common.repository.CommentRepository;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminBoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;


    public Long writeComment(Long boardId, String content, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        AdminBoard board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        Comment result = Comment.builder()
                .content(content)
                .adminBoard(board)
                .user(user)
                .build();
        commentRepository.save(result);

        return result.getId();
    }


    public List<CommentDto> commentList(Long id) {
        AdminBoard board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        List<Comment> comments = commentRepository.findByAdminBoard(board);

        return comments.stream()
                .map(comment -> CommentDto.entityToDto(comment, comment.getAdminBoard(), comment.getUser()))
                .collect(Collectors.toList());
    }


    public void updateComment(String content, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comment.update(content);
        commentRepository.save(comment);
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}