package com.example.ITHOTEL.service;

import com.example.ITHOTEL.domain.common.entity.Hotel;
import com.example.ITHOTEL.domain.common.entity.HotelFileInfo;
import com.example.ITHOTEL.domain.common.repository.HotelFileInfoRepository;
import com.example.ITHOTEL.domain.common.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HomeService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelFileInfoRepository hotelFileInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> getDistinctRegion() {
        return hotelRepository.findDistinctRegion();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<HotelFileInfo> getHotelMainFileInfoList() {
        return hotelFileInfoRepository.findByIsMainImage(true);
    }
}
