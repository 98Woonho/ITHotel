package com.example.ITHOTEL.domain.common.repository;

import com.example.ITHOTEL.domain.common.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {
    List<Hotel> findAll();

    @Query("SELECT DISTINCT h.region FROM Hotel h")
    List<String> findDistinctRegion();

    @Query("SELECT h.hotelName FROM Hotel h")
    List<String> findAllHotelName();
}