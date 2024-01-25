package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminBoardService {
    public Long saveBoard(BoardWriteRequestDto boardWriteRequestDto, String email);
    public BoardResponseDto boardDetail(Long id);
    public Page<BoardResponseDto> boardList(Pageable pageable);
    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto);
    public void boardRemove(Long id);
    public Page<CommentResponseDto> commentList(Pageable pageable);
    Page<BoardResponseDto> searchingBoardList(String keyword, String type, Pageable pageable);
}
