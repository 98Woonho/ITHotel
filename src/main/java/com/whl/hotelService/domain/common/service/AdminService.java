package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import com.whl.hotelService.domain.common.repository.HotelFileInfoRepository;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import com.whl.hotelService.domain.common.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelFileInfoRepository hotelFileInfoRepository;


    public List<String> getRegionList() {
        return hotelRepository.findDistinctRegion();
    }

    public List<String> getHotelList() {
        return hotelRepository.findAllHotelname();
    }

    public Hotel getHotel(String hotelname) {
        return hotelRepository.findById(hotelname).get();
    }

    public List<HotelFileInfo> getHotelFileInfo(String hotelname) {
        return hotelFileInfoRepository.findByHotelHotelname(hotelname);
    }
}
