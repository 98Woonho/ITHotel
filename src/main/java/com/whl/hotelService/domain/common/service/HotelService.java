package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getHotel() {
        return hotelRepository.findAll();
    }

    public List<String> getRegion() {
        return hotelRepository.findDistinctRegion();
    }
}
