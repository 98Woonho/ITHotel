package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoard(Board board);
}
