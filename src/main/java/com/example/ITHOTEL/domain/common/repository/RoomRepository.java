package com.example.ITHOTEL.domain.common.repository;

import com.example.ITHOTEL.domain.common.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelHotelNameAndStandardPeopleGreaterThanEqual(String hotelName, int people);

    @Query("SELECT r.kind FROM Room r WHERE r.hotel.hotelName = :hotelName")
    List<String> findHotelsRoomKind(@Param("hotelName") String hotelName);

    void deleteByHotelHotelNameAndKind(String hotelName, String kind);

    void deleteByHotelHotelName(String hotelName);

    List<Room> findByHotelHotelName(String hotelName);
}
