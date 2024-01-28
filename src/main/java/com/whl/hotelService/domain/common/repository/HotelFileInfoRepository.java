package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelFileInfoRepository extends JpaRepository<HotelFileInfo, Long> {
    @Query("SELECT h FROM HotelFileInfo h WHERE h.hotel.hotelname = :hotelname")
    List<HotelFileInfo> findByHotelName(String hotelname);

    void deleteByFilenameAndHotelHotelname(String filename, String hotelname);
}
