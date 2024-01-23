package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.common.entity.Comment;
import com.whl.hotelService.domain.common.repository.AdminBoardRepository;
import com.whl.hotelService.domain.common.repository.CommentRepository;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminBoardServiceImpl implements AdminBoardService{

    @Autowired
    private AdminBoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Long saveBoard(BoardWriteRequestDto boardWriteRequestDto, String id) {
        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));

        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        AdminBoard result = AdminBoard.builder()
                .title(boardWriteRequestDto.getTitle())
                .content(boardWriteRequestDto.getContent())
                .user(user)
                .build();

        // Board 엔터티를 데이터베이스에 저장
        boardRepository.save(result);

        return result.getId();
    }

    @Override
    public BoardResponseDto boardDetail(Long id) {
        AdminBoard board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = board.getUser();
        BoardResponseDto result = BoardResponseDto.entityToDto(board, user);

        return result;
    }

    @Override
    public Page<BoardResponseDto> boardList(Pageable pageable) {
        Page<AdminBoard> boards = boardRepository.findAll(pageable);
        return getBoardResponseDtos(pageable, boards);
    }
    @Override
    public Page<CommentResponseDto> commentList(Pageable pageable) {
        Page<Comment> comments  = commentRepository.findAll(pageable);
        List<CommentResponseDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments){
            User user = comment.getUser();
            AdminBoard board = comment.getAdminBoard();
            CommentResponseDto result = CommentResponseDto.entityToDto(comment, board, user);
            commentDtos.add(result);
        }
        return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
    }
    @Override
    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto) {
        AdminBoard board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.update(boardWriteRequestDto.getTitle(), boardWriteRequestDto.getContent());
        boardRepository.save(board);

        return board.getId();
    }
    @Override
    public void boardRemove(Long id) {
        AdminBoard board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        boardRepository.delete(board);
    }

    @Override
    public Page<BoardResponseDto> searchingBoardList(String keyword, String type, Pageable pageable) {
        Page<AdminBoard> boards = boardRepository.searchBoards(keyword, type, pageable);
        return getBoardResponseDtos(pageable, boards);
    }


    //    Entity를 Dto로 변환
    private PageImpl<BoardResponseDto> getBoardResponseDtos(Pageable pageable, Page<AdminBoard> boards) {
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : boards) {
            User user = board.getUser();
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

}
