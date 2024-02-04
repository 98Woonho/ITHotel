package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.Comment;
import com.whl.hotelService.domain.common.repository.AdminBoardRepository;
import com.whl.hotelService.domain.common.repository.CommentRepository;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminBoardService {

    @Autowired
    private AdminBoardRepository adminBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Long saveBoard(String hotelname, String relation, BoardWriteRequestDto boardWriteRequestDto, String id) {
        boardWriteRequestDto.setHotelname(hotelname);
        boardWriteRequestDto.setRelation(relation);
        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        AdminBoard result = AdminBoard.builder()
                .title(boardWriteRequestDto.getTitle())
                .content(boardWriteRequestDto.getContent())
                .user(user)
                .hotelname(boardWriteRequestDto.getHotelname())
                .relation((boardWriteRequestDto.getRelation()))
                .build();
        // Board 엔터티를 데이터베이스에 저장
        adminBoardRepository.save(result);

        return result.getId();
    }


    public BoardResponseDto boardDetail(Long id) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = board.getUser();
        BoardResponseDto result = BoardResponseDto.entityToDto(board, user);

        return result;
    }


    public Page<BoardResponseDto> boardList(Pageable pageable) {
        Page<AdminBoard> boards = adminBoardRepository.findAll(pageable);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : boards) {
            User user = board.getUser();
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    public Page<CommentResponseDto> commentList(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        List<CommentResponseDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            User user = comment.getUser();
            AdminBoard board = comment.getAdminBoard();
            CommentResponseDto result = CommentResponseDto.entityToDto(comment, board, user);
            commentDtos.add(result);
        }
        return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
    }

    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.update(boardWriteRequestDto.getTitle(), boardWriteRequestDto.getContent());
        adminBoardRepository.save(board);

        return board.getId();
    }

    public void boardRemove(Long id) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        adminBoardRepository.delete(board);
    }


    public Page<BoardResponseDto> searchingBoardList(String keyword, String type, Pageable pageable) {
        Page<AdminBoard> boards = adminBoardRepository.searchBoards(keyword, type, pageable);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : boards) {
            User user = board.getUser();
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    public Page<BoardResponseDto> userBoardList(Pageable pageable, Authentication authentication){
        Page<AdminBoard> adminBoards = adminBoardRepository.findByUserUserid(pageable, authentication.getName());
        List<BoardResponseDto> boardDtos = new ArrayList<>();
            for (AdminBoard board: adminBoards){
               User user = board.getUser();
                System.out.println("Authentication" + authentication.getName());
                BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
                boardDtos.add(result);
            }
            return new PageImpl<>(boardDtos, pageable, adminBoards.getTotalElements());
    }

}
