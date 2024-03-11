package com.example.ITHOTEL.domain.common.repository;

import com.example.ITHOTEL.domain.common.entity.NoticeBoardFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardFileInfoRepository extends JpaRepository<NoticeBoardFileInfo, Long> {
    NoticeBoardFileInfo findByNoticeBoardId(Long id);
}
