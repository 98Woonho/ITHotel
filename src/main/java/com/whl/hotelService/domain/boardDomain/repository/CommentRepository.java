package com.whl.hotelService.domain.boardDomain.repository;

import com.whl.hotelService.domain.boardDomain.entity.BoardEntity;
import com.whl.hotelService.domain.boardDomain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity); //
}
