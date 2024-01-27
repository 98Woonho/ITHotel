package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelFileInfoRepository extends JpaRepository<HotelFileInfo, Long> {

}
