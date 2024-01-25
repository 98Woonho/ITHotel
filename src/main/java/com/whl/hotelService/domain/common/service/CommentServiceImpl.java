package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.CommentRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.common.entity.Comment;
import com.whl.hotelService.domain.common.repository.AdminBoardRepository;
import com.whl.hotelService.domain.common.repository.BoardRepository;
import com.whl.hotelService.domain.common.repository.CommentRepository;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminBoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Long writeComment(CommentRequestDto commentRequestDto, Long boardId, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        AdminBoard board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        Comment result = Comment.builder()
                .content(commentRequestDto.getContent())
                .adminBoard(board)
                .user(user)
                .build();
        commentRepository.save(result);

        return result.getId();
    }

    @Override
    public List<CommentResponseDto> commentList(Long id) {
        AdminBoard board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        List<Comment> comments = commentRepository.findByAdminBoard(board);

        return comments.stream()
                .map(comment -> CommentResponseDto.entityToDto(comment, comment.getAdminBoard(), comment.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateComment(CommentRequestDto commentRequestDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comment.update(commentRequestDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}