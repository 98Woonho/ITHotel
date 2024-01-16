package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;

import java.util.List;

public interface BoardService {
    public Long saveBoard(BoardWriteRequestDto boardWriteRequestDto, String email);
    public BoardResponseDto boardDetail(Long id);
    public List<BoardResponseDto> boardList();
    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto);
    public void boardRemove(Long id);
}
