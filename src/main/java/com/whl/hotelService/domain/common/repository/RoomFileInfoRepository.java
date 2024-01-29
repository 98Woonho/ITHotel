package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.RoomFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomFileInfoRepository extends JpaRepository<RoomFileInfo, Long> {
    @Query("SELECT r FROM RoomFileInfo r WHERE r.room.hotel.hotelName = :hotelName AND r.isMainImage = true")
    List<RoomFileInfo> findAllMainFiles(String hotelName);

    @Query("SELECT r FROM RoomFileInfo r WHERE r.room.id = :id AND r.isMainImage = true")
    RoomFileInfo findRoomsMainFile(Long id);

    RoomFileInfo findByRoomKindAndRoomHotelHotelNameAndIsMainImage(String roomKind, String hotelName, boolean isMainImage);

    List<RoomFileInfo> findAllByRoomKindAndRoomHotelHotelNameAndIsMainImage(String roomKind, String hotelName, boolean isMainImage);

    void deleteByFileNameAndRoomHotelHotelName(String fileName, String hotelName);
}