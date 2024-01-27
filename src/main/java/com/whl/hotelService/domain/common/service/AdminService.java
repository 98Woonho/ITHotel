package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private HotelRepository hotelRepository;

    public List<String> getRegionList() {
        return hotelRepository.findDistinctRegion();
    }
}
