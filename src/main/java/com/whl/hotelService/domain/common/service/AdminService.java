package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.entity.*;
import com.whl.hotelService.domain.common.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelFileInfoRepository hotelFileInfoRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomFileInfoRepository roomFileInfoRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    public List<String> getRegionList() {
        return hotelRepository.findDistinctRegion();
    }

    public List<String> getHotelNameList() {
        return hotelRepository.findAllHotelName();
    }

    public Hotel getHotel(String hotelName) {
        return hotelRepository.findById(hotelName).get();
    }

    public List<String> getRoomKindList(String hotelName) {
        return roomRepository.findHotelsRoomKind(hotelName);
    }

    public HotelFileInfo getHotelMainFileInfo(String hotelName, boolean isMainImage) {
        return hotelFileInfoRepository.findByHotelHotelNameAndIsMainImage(hotelName, isMainImage);
    }

    public List<HotelFileInfo> getHotelFileInfoList(String hotelName, boolean isMainImage) {
        return hotelFileInfoRepository.findAllByHotelHotelNameAndIsMainImage(hotelName, isMainImage);
    }

    public RoomFileInfo getRoomMainFileInfo(String hotelName, String roomKind, boolean isMainImage) {
        return roomFileInfoRepository.findByRoomKindAndRoomHotelHotelNameAndIsMainImage(roomKind, hotelName, isMainImage);
    }

    public List<RoomFileInfo> getRoomFileList(String hotelName, String roomKind, boolean isMainImage) {
        return roomFileInfoRepository.findAllByRoomKindAndRoomHotelHotelNameAndIsMainImage(roomKind, hotelName, isMainImage);
    }

    public List<Payment> getAllPaymentList() {
        return paymentRepository.findAll();
    }

    public List<Hotel> getAllHotelList() {
        return hotelRepository.findAll();
    }

    public List<Room> getRoomList(String hotelName) {
        return roomRepository.findByHotelHotelName(hotelName);
    }

    public List<Payment> getPaymentListByRegion(String region) {
        return paymentRepository.findAllByReservationRoomHotelRegion(region);
    }

    public List<Payment> getPaymentListByHotelName(String hotelName) {
        return paymentRepository.findAllByReservationRoomHotelHotelName(hotelName);
    }

    public List<Reservation> getAllReservationList() {
        return reservationRepository.findAll();
    }
}
