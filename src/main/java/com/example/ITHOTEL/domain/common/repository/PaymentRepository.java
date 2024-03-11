package com.example.ITHOTEL.domain.common.repository;

import com.example.ITHOTEL.domain.common.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByReservationRoomHotelHotelName(String hotelName);

    List<Payment> findAllByReservationRoomHotelRegion(String region);

    Payment findByReservationId(Long id);
}
