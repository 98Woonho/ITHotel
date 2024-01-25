package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.common.entity.Comment;
import jakarta.persistence.NamedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b " +
            "WHERE (:type = 'T' AND b.title LIKE CONCAT('%', :keyword, '%')) " +
            "   OR (:type = 'C' AND b.content like concat('%', :keyword, '%'))" +
            "   OR (:type = 'W' AND b.user.userid LIKE CONCAT('%', :keyword, '%'))" +
            "ORDER BY b.createdTime asc")
    Page<Board> searchBoards(@Param("keyword") String keyword, @Param("type") String type, Pageable pageable);

    List<Board> findByBoardType(String boardType);
}
