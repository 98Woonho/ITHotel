package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.Reservation;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByUserUserid(String userid);

    Reservation findByUserUseridAndStatus(String userid, String status);

    @Query("SELECT COUNT(e) FROM Reservation e WHERE e.room.id = :roomId")
    int findReservedRoomCount(Long roomId);
}
