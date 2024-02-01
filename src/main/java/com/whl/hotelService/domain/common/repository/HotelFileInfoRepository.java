package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelFileInfoRepository extends JpaRepository<HotelFileInfo, Long> {
    List<HotelFileInfo> findByHotelHotelName(String hotelName);

    void deleteByFileNameAndHotelHotelName(String fileName, String hotelName);
}
