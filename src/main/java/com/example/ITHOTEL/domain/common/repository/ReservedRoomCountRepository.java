package com.example.ITHOTEL.domain.common.repository;


import com.example.ITHOTEL.domain.common.entity.ReservedRoomCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservedRoomCountRepository extends JpaRepository<ReservedRoomCount, Long> {
    ReservedRoomCount findByDateAndRoomId(String date, Long roomId);

}
