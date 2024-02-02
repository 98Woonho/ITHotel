package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelFileInfoRepository extends JpaRepository<HotelFileInfo, Long> {
    List<HotelFileInfo> findByIsMainImage(boolean isMainImage);

    void deleteByFileNameAndHotelHotelName(String fileName, String hotelName);

    HotelFileInfo findByHotelHotelNameAndIsMainImage(String hotelName, boolean isMainImage);

    List<HotelFileInfo> findAllByHotelHotelNameAndIsMainImage(String hotelName, boolean isMainImage);
}
