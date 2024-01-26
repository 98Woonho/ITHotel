package com.whl.hotelService.domain.common.repository;

import com.whl.hotelService.domain.common.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r.createdAt FROM Reservation r WHERE r.status = :status")
    List<LocalDateTime> findCreatedAtByStatus(String status);

    Reservation findByUserUseridAndStatus(String userid, String status);

    void deleteByCreatedAt(LocalDateTime createdAt);
}
