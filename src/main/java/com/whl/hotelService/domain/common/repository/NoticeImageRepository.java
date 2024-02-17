package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.NoticeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {

    List<NoticeImage> findByNoticeBoardId(Long id);
}
