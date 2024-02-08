package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.common.entity.NoticeBoard;
import com.whl.hotelService.domain.common.entity.NoticeBoardFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardFileInfoRepository extends JpaRepository<NoticeBoardFileInfo, Long> {
    NoticeBoardFileInfo findByNoticeBoardId(Long id);
}
