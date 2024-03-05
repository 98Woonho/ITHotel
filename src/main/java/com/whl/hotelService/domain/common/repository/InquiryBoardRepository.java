package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.InquiryBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryBoardRepository extends JpaRepository<InquiryBoard, Long> {
    @Query("SELECT b FROM InquiryBoard b " +
            "WHERE (:type = 'T' AND b.title LIKE CONCAT('%', :keyword, '%')) " +
            "   OR (:type = 'C' AND b.content like concat('%', :keyword, '%'))" +
            "   OR (:type = 'W' AND b.user.name LIKE CONCAT('%', :keyword, '%'))" +
            "   OR (:type = 'H' AND b.hotelname LIKE concat('%', :keyword, '%'))" +
            "   OR (:type = 'Q' AND b.relation LIKE concat('%', :keyword, '%'))" +
            "ORDER BY b.createdTime asc")
    Page<InquiryBoard> searchBoards(@Param("keyword") String keyword, @Param("type") String type, Pageable pageable);
    Page<InquiryBoard> findByUserUserid(Pageable pageable, String userid);
}
