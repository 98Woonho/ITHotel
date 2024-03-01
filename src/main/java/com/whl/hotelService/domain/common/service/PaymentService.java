package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.PaymentDto;
import com.whl.hotelService.domain.common.entity.*;
import com.whl.hotelService.domain.common.repository.*;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomFileInfoRepository roomFileInfoRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean addPayment(PaymentDto paymentDto) {
        Reservation reservation = reservationRepository.findById(paymentDto.getReservationId()).get();
        reservation.setStatus("예약 완료");
        reservationRepository.save(reservation);

        Payment payment = new Payment();
        payment.setAddress(paymentDto.getAddress());
        payment.setImpUid(paymentDto.getImpUid());
        payment.setName(paymentDto.getName());
        payment.setPaidAmount(paymentDto.getPaidAmount());
        payment.setPayMethod(paymentDto.getPayMethod());
        payment.setMerchantUid(paymentDto.getMerchantUid());
        payment.setStatus(paymentDto.getStatus());
        payment.setPayDate(LocalDateTime.now());
        payment.setReservation(reservation);

        paymentRepository.save(payment);

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean isDeleteReservation(String reservationId) {
        reservationRepository.deleteById(Long.valueOf(reservationId));

        return true;
    }

    public RoomFileInfo getRoomsMainFileInfo(Long id) {
        return roomFileInfoRepository.findRoomsMainFile(id);
    }
}
