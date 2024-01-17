package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.CommentRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    Long writeComment(CommentRequestDto commentRequestDto, Long boardId, String email);

    List<CommentResponseDto> commentList(Long id);

    void updateComment(CommentRequestDto commentRequestDto, Long commentId);

    void deleteComment(Long commentId);
}
