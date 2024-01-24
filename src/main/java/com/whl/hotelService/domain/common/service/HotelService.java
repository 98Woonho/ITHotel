package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.PaymentDto;
import com.whl.hotelService.domain.common.dto.ReservationDto;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservedRoomCountRepository reservedRoomCountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> getDistinctRegion() {
        return hotelRepository.findDistinctRegion();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Room> getHotelsRoom(String hotelname, int people) {
        return roomRepository.findByHotelHotelnameAndStandardPeopleGreaterThanEqual(hotelname, people);
    }

    public void addReservedRoomCount(Long id, int reservedRoomCount) {
        Room room = roomRepository.findById(id).get();

        room.setReservedRoomCount(reservedRoomCount);

        roomRepository.save(room);
    }

    @Transactional(rollbackFor = Exception.class)
    public int getReservedRoomCount(String date, Long roomId) {
        ReservedRoomCount reservedRoomCount = reservedRoomCountRepository.findByDateAndRoomId(date, roomId);

        return (reservedRoomCount != null) ? reservedRoomCount.getReservedCount() : 0;
    }

    public String addReservedRoomCount(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).get();

        Room room = reservation.getRoom();

        // Parse the strings to LocalDate objects
        LocalDate date1 = LocalDate.parse(reservation.getCheckin());
        LocalDate date2 = LocalDate.parse(reservation.getCheckout());

        // Calculate the number of days between the two dates
        long daysDifference = ChronoUnit.DAYS.between(date1, date2);

        // Create a list containing LocalDate objects for each day between the two dates
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

                try {
                    reservedRoomCountRepository.save(existingReservedRoomCount);
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return "FAILURE";
                }
            } else {
                ReservedRoomCount reservedRoomCount = new ReservedRoomCount();
                reservedRoomCount.setDate(String.valueOf(date));
                reservedRoomCount.setRoom(room);

                try {
                    reservedRoomCountRepository.save(reservedRoomCount);
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    return "FAILURE";
                }
            }
        }
        return "SUCCESS";
    }

    @Transactional
    public boolean insertReservation(ReservationDto reservationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();

        User user = userRepository.findById(userid).get();

        Room room = roomRepository.findById(reservationDto.getRoomId()).get();

        String status = reservationDto.getStatus();

        if (user != null && room != null) {
            // Check if a reservation already exists for the user and room
            Reservation existingReservation = reservationRepository.findByUserUseridAndStatus(userid, status);

            if (existingReservation != null) {
                // Update existing reservation
                existingReservation.setCheckin(reservationDto.getCheckin());
                existingReservation.setCheckout(reservationDto.getCheckout());
                existingReservation.setRoom(room);
                existingReservation.setPeople(reservationDto.getPeople());
                existingReservation.setCreatedAt(new Date());
                existingReservation.setPrice(reservationDto.getPrice());

                reservationRepository.save(existingReservation);

            } else {
                // Create a new reservation
                Reservation reservation = new Reservation();
                reservation.setRoom(room);
                reservation.setUser(user);
                reservation.setStatus(reservationDto.getStatus());
                reservation.setCheckin(reservationDto.getCheckin());
                reservation.setCheckout(reservationDto.getCheckout());
                reservation.setPeople(reservationDto.getPeople());
                reservation.setCreatedAt(new Date());
                reservation.setPrice(reservationDto.getPrice());

                reservationRepository.save(reservation);

            }
        }
        return true;
    }

    @Transactional
    public Reservation getReservationList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        return reservationRepository.findByUserUseridAndStatus(userid, "예약 중");
    }

    @Transactional
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findById(username).get();
        payment.setUser(user);

        paymentRepository.save(payment);

        return true;

    }
}
