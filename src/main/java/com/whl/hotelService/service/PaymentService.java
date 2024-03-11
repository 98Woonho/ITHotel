package com.whl.hotelService.service;

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

    @Autowired
    private ReservedRoomCountRepository reservedRoomCountRepository;

    @Transactional(rollbackFor = Exception.class)
    public Reservation getReservationList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        return reservationRepository.findByUserUseridAndStatus(userid, "예약 중");
    }

    @Transactional(rollbackFor = Exception.class)
    public String addReservedRoomCount(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).get();

        Room room = reservation.getRoom();

        // 날짜 문자열을 LocalDate로 변환
        LocalDate date1 = LocalDate.parse(reservation.getCheckin());
        LocalDate date2 = LocalDate.parse(reservation.getCheckout());

        // 두 날짜 사이의 일수 계산
        long daysDifference = ChronoUnit.DAYS.between(date1, date2);

        // 두 날짜를 포함한 사이의 날짜 List 생성
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i <= daysDifference - 1; i++) {
            dateList.add(date1.plusDays(i));
        }

        for (LocalDate date : dateList) {
            ReservedRoomCount existingReservedRoomCount = reservedRoomCountRepository.findByDateAndRoomId(String.valueOf(date), room.getId());

            if (existingReservedRoomCount != null) {
                if (existingReservedRoomCount.getReservedCount() == room.getCount()) {
                    reservationRepository.deleteById(reservationId);
                    return "FAILURE_NOVACANCY";
                } else {
                    existingReservedRoomCount.setReservedCount(existingReservedRoomCount.getReservedCount() + 1);
                }

                reservedRoomCountRepository.save(existingReservedRoomCount);

            } else {
                ReservedRoomCount reservedRoomCount = new ReservedRoomCount();
                reservedRoomCount.setDate(String.valueOf(date));
                reservedRoomCount.setRoom(room);

                reservedRoomCountRepository.save(reservedRoomCount);
            }
        }
        return "SUCCESS";
    }

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

    public RoomFileInfo getRoomsMainFileInfo(Long id) {
        return roomFileInfoRepository.findRoomsMainFile(id);
    }
}
