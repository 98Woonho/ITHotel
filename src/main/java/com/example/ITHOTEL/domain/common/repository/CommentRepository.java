package com.example.ITHOTEL.domain.common.repository;

import com.example.ITHOTEL.domain.common.entity.InquiryBoard;
import com.example.ITHOTEL.domain.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment>findByInquiryboard(InquiryBoard board);
}
