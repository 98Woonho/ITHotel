package com.whl.hotelService.domain.common.repository;


import com.whl.hotelService.domain.common.entity.ReservedRoomCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservedRoomCountRepository extends JpaRepository<ReservedRoomCount, Long> {
    ReservedRoomCount findByDateAndRoomId(String date, Long roomId);

}
