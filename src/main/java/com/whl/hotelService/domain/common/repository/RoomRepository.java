package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelHotelname(String hotelName);
}
