package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.common.entity.RoomFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelHotelnameAndStandardPeopleGreaterThanEqual(String hotelName, int people);

    @Query("SELECT r.id FROM Room r WHERE r.hotel.hotelname = :hotelname")
    Long[] findHotelsRoomId(String hotelname);

    @Query("SELECT r.kind FROM Room r WHERE r.hotel.hotelname = :hotelname")
    List<String> findHotelsRoomKind(String hotelname);
}
