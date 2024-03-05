package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.NoticeBoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeImageRepository extends JpaRepository<NoticeBoardImage, Long> {

    List<NoticeBoardImage> findByNoticeBoardId(Long id);
}
