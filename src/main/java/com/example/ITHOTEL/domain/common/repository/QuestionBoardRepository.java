package com.example.ITHOTEL.domain.common.repository;

import com.example.ITHOTEL.domain.common.entity.QuestionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    @Query("SELECT b FROM QuestionBoard b " +
            "WHERE (:type = 'T' AND b.title LIKE CONCAT('%', :keyword, '%')) " +
            "   OR (:type = 'C' AND b.content like concat('%', :keyword, '%'))" +
            "   OR (:type = 'W' AND b.user.userid LIKE CONCAT('%', :keyword, '%'))" +
            "ORDER BY b.createdTime asc")
    Page<QuestionBoard> searchBoards(@Param("keyword") String keyword, @Param("type") String type, Pageable pageable);

}
