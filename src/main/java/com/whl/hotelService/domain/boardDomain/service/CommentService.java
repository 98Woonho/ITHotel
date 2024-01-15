package com.whl.hotelService.domain.boardDomain.service;

import com.whl.hotelService.domain.boardDomain.dto.CommentDto;
import com.whl.hotelService.domain.boardDomain.entity.BoardEntity;
import com.whl.hotelService.domain.boardDomain.entity.CommentEntity;
import com.whl.hotelService.domain.boardDomain.repository.BoardRepository;
import com.whl.hotelService.domain.boardDomain.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long save(CommentDto commentDto) throws Exception{
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDto.getBoardId());
        if (optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.DtoToSaveEntity(commentDto, boardEntity);
            return commentRepository.save(commentEntity).getId();
        }else {
            return null;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public List<CommentDto> findAll(Long boardId) throws Exception {
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList){
            CommentDto commentDto = CommentDto.EntityToCommentDto(commentEntity, boardId);
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }
}
